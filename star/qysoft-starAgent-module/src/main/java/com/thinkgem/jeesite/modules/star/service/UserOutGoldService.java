/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.service;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils2;
import com.thinkgem.jeesite.modules.md.entity.GoodsStatus;
import com.thinkgem.jeesite.modules.md.entity.MdTradeMain;
import com.thinkgem.jeesite.modules.md.service.MdTradeMainService;
import com.thinkgem.jeesite.modules.md.service.MdTradeService;
import com.thinkgem.jeesite.modules.star.dao.UserColectionDao;
import com.thinkgem.jeesite.modules.star.entity.BaseGoodsGroup;
import com.thinkgem.jeesite.modules.star.entity.EverydayHold;
import com.thinkgem.jeesite.modules.star.entity.TempTrans;
import com.thinkgem.jeesite.modules.star.entity.UserColection;
import com.thinkgem.jeesite.modules.user.dao.UserUserinfoDao;
import com.thinkgem.jeesite.modules.user.entity.UserTeamLevel;
import com.thinkgem.jeesite.modules.user.entity.UserUserinfo;
import com.thinkgem.jeesite.modules.user.service.UserTeamLevelService;
import com.thinkgem.jeesite.modules.user.service.UserUserinfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 收藏表Service
 * @author luojie
 * @version 2017-09-28
 */
@Service
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class UserOutGoldService extends CrudService<UserColectionDao, UserColection> {

	@Resource
	private UserUserinfoService userUserinfoService;
	@Resource
	private EverydayHoldService everydayHoldService;
	@Resource
	private UserTeamLevelService teamLevelService;
	@Resource
	private MdTradeService mdTradeService;
	@Resource
	private MdTradeMainService mdTradeMainService;
	@Resource
	private TempTransService tempTransService;
	@Resource
	private UserUserinfoDao userUserinfoDao;
	@Resource
	private BaseGoodsGroupService baseGoodsGroupService;

	private ReentrantLock lock = new ReentrantLock();


	public void userOutGold(String userName ,BigDecimal chujinMoney) throws ValidationException{
		try {
			lock.lock();
			UserUserinfo byNameLock = userUserinfoService.getByNameLock(userName);
			if(byNameLock == null){
				throw new ValidationException("出金用户不存在!");
			}
			if(byNameLock.getMoney2().compareTo(chujinMoney)<0){
				throw new ValidationException("资金账户余额不足!");
			}
			if(StringUtils.isBlank(byNameLock.getUsercenterAddress())){
				throw new ValidationException("还没有拍卖到产品!");
			}
			if(chujinMoney.compareTo(BigDecimal.ZERO)<=0){
				throw new ValidationException("出金必須大于0!");
			}

			BaseGoodsGroup baseGoodsGroup = baseGoodsGroupService.get(byNameLock.getUsercenterAddress());
			if(baseGoodsGroup == null){
				throw new ValidationException("拍卖到的产品不存在!");
			}

			BigDecimal cxMoney;
			BigDecimal bzjMoney;
			BigDecimal glfMoney;
			BigDecimal dlsMoney;
			Date beginTime;
			Date endTime;
			int outDay;
			BigDecimal outScale;
			try {
				beginTime = DateUtils2.formatStrTime(Global.getOption("md_config", "cj_time_begin"));
				endTime = DateUtils2.formatStrTime(Global.getOption("md_config", "md_time_begin"));
				outDay = Integer.parseInt(Global.getOption("md_config", "out_day", "30"));
				outScale = new BigDecimal(Global.getOption("md_config", "out_scale", "0.3"));
				cxMoney = chujinMoney.multiply(new BigDecimal(Global.getOption("md_config", "cx_money", "0.5"))).setScale(2,BigDecimal.ROUND_HALF_UP);
				bzjMoney = chujinMoney.multiply( new BigDecimal(Global.getOption("md_config","bzj_money","0.1"))).setScale(2,BigDecimal.ROUND_HALF_UP);
				glfMoney =  chujinMoney.multiply(new BigDecimal(Global.getOption("md_config","glf_money","0.05"))).setScale(2,BigDecimal.ROUND_HALF_UP);
				dlsMoney =  chujinMoney.multiply(new BigDecimal(Global.getOption("md_config","dls_money","0.35"))).setScale(2,BigDecimal.ROUND_HALF_UP);
			} catch (Exception e) {
				throw new ValidationException("出金配置错误!");
			}
			Date nowtime = DateUtils2.formatStrTime(DateUtils2.getTime());
			if (nowtime.getTime() < beginTime.getTime() || nowtime.getTime() > endTime.getTime()){
				throw new ValidationException("不在出金时间内");
			}

			//查询交易日
			MdTradeMain selectMain = new MdTradeMain();
			selectMain.setGroupId(byNameLock.getUsercenterAddress());
			selectMain.setIsTransDay(EnumUtil.YesNO.YES.toString());
			selectMain.setCreateDateTimeBegin(byNameLock.getActiveTime());
			selectMain.setCreateDateEnd(DateUtils2.getDiffDatetime(new Date(),-1));
			int transDay = mdTradeMainService.findList(selectMain).size();
			if(GoodsStatus.MD.toString().equals(baseGoodsGroup.getStatus())){
				if(transDay < outDay){
					throw new ValidationException("距离开盘或上次出金未达到 "+outDay+"个交易日");
				}
				BigDecimal maxOutMoney = byNameLock.getMoney2().multiply(outScale).setScale(2,BigDecimal.ROUND_HALF_UP);
				if(chujinMoney.compareTo(maxOutMoney)>0){
					throw new ValidationException("每次出金最多不能超过"+maxOutMoney);
				}
			}
			//处理承销商
			userUserinfoService.updateUserOtherMoney(userName,chujinMoney.multiply(BigDecimal.valueOf(-1)), EnumUtil.MoneyType.money2,"出金扣除保证金:"+bzjMoney+"扣除管理费:"+glfMoney+"代理商分配:"+dlsMoney, EnumUtil.MoneyChangeType.MD_OUT_GLOD);
			userUserinfoService.updateUserMoney(userName,cxMoney.setScale(2,BigDecimal.ROUND_HALF_UP),"出金收入", EnumUtil.MoneyChangeType.MD_OUT_GLOD);
			userUserinfoService.updateUserOtherMoney(userName,bzjMoney, EnumUtil.MoneyType.money3,"出金保证金", EnumUtil.MoneyChangeType.MD_OUT_GLOD);
			//查询所有代理商
			UserUserinfo userUserinfo = new UserUserinfo();
			userUserinfo.setUserTypeBegin("1");
			List<UserUserinfo> dlUserlist = userUserinfoService.findList(userUserinfo);
			//查询日持仓
			EverydayHold select = new EverydayHold();
			select.setCreateDateTimeBegin(byNameLock.getActiveTime());
			select.setGroupId(byNameLock.getUsercenterAddress());
			List<EverydayHold> holdList = everydayHoldService.findList(select);
			BigDecimal allAveHold = BigDecimal.ZERO;
			if (transDay > 0) {
				for(UserUserinfo userinfo:dlUserlist){
					BigDecimal userNum = holdList.stream().filter(p -> (p.getParentName().equals(userinfo.getUserName()) && EnumUtil.YesNO.NO.toString().equals((p.getUserType())))  || p.getUserName().equals(userinfo.getUserName())).map(p->BigDecimal.valueOf(p.getNum()).multiply(p.getMoney())).reduce(BigDecimal.ZERO,BigDecimal::add);
					BigDecimal aveHold = userNum.divide(BigDecimal.valueOf(transDay),2,BigDecimal.ROUND_HALF_UP);
					allAveHold = allAveHold.add(aveHold);
				}
			}

			List<UserTeamLevel> teamLevelList = teamLevelService.findList(new UserTeamLevel());

			tempTransService.clearTable();
			//给所有用户

			if (allAveHold.compareTo(BigDecimal.ZERO)>0) {
				for(UserUserinfo userinfo:dlUserlist){
					Optional<UserTeamLevel> userOptional = teamLevelList.stream().filter(p -> p.getTeamCode().equals(userinfo.getUserType())).findFirst();
					if(!userOptional.isPresent()){
						continue;
					}
					UserTeamLevel userTeamLevel = userOptional.get();
					BigDecimal sonScale = new BigDecimal(userTeamLevel.getDirectEarnings());
					BigDecimal aveHold = holdList.stream().filter(p -> (p.getParentName().equals(userinfo.getUserName()) && EnumUtil.YesNO.NO.toString().equals((p.getUserType()))) || p.getUserName().equals(userinfo.getUserName())).map(p->BigDecimal.valueOf(p.getNum()).multiply(p.getMoney())).reduce(BigDecimal.ZERO,BigDecimal::add).divide(BigDecimal.valueOf(transDay),6,BigDecimal.ROUND_HALF_UP);
					BigDecimal money = chujinMoney.multiply(new BigDecimal(userTeamLevel.getDirectEarnings())).multiply(aveHold).divide(allAveHold,2,BigDecimal.ROUND_HALF_UP);
					tempTransService.updateMoneyByName(userinfo.getUserName(),money);
					String[] userArray = userinfo.getParentList().split("/");
					for(String userId : userArray){
						if(StringUtils.isBlank(userId)){
							continue;
						}
						UserUserinfo parentUser = userUserinfoService.get(userId);
						int parentCode = 0;
						try {
							parentCode = Integer.parseInt(parentUser.getUserType());
						} catch (NumberFormatException e) {
							continue;
						}
						if(parentCode <= 1){
							continue;
						}
						Optional<UserTeamLevel> parentOptional = teamLevelList.stream().filter(p -> p.getTeamCode().equals(parentUser.getUserType())).findFirst();
						if(!parentOptional.isPresent()){
							continue;
						}
						BigDecimal parentScale = new BigDecimal(parentOptional.get().getDirectEarnings()).subtract(sonScale);
						BigDecimal parentMoney =chujinMoney.multiply(parentScale).multiply(aveHold).divide(allAveHold,2,BigDecimal.ROUND_HALF_UP);
						sonScale = new BigDecimal(parentOptional.get().getDirectEarnings());
						if(parentMoney.compareTo(BigDecimal.ZERO)<=0){
							continue;
						}
						tempTransService.updateMoneyByName(parentUser.getUserName(),parentMoney);
					}
				}
			}
			//发奖励
			List<TempTrans> bonusList =  tempTransService.findList(new TempTrans());
			for(TempTrans bonusUser: bonusList) {
				userUserinfoService.updateUserMoney(bonusUser.getUserName(), bonusUser.getMoney(), "出金奖励,出金承销商: " + byNameLock.getUserName(), EnumUtil.MoneyChangeType.MD_OUT_GLOD_REBATE);
			}
			userUserinfoDao.updateActiveTime(byNameLock.getId(),new Date());
		} catch (ValidationException e) {
			throw new ValidationException(e.getMessage());
		} catch (Exception e) {
			throw new ValidationException("出金错误");
		} finally {
			lock.unlock();
		}
	}
}