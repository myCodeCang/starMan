/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.service;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.trans.dao.TransApplyDao;
import com.thinkgem.jeesite.modules.trans.dao.TransGoodsGroupDao;
import com.thinkgem.jeesite.modules.trans.entity.*;
import com.thinkgem.jeesite.modules.user.dao.UserAddressDao;
import com.thinkgem.jeesite.modules.user.entity.UserAddress;
import com.thinkgem.jeesite.modules.user.entity.UserUserinfo;
import com.thinkgem.jeesite.modules.user.service.UserUserinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订货表Service
 * @author xueyuliang
 * @version 2017-05-08
 */
@Service
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class TransApplyService extends CrudService<TransApplyDao, TransApply> {


	@Autowired
	private UserUserinfoService userUserinfoService;

	@Autowired
	private TransGoodsService transGoodsService;
	@Autowired
	private TransBuyLogService transBuyLogService;

	@Autowired
	private TransGoodsGroupDao transGoodsGroupDao ;

	@Autowired
	private UserAddressDao userAddressDao ;

	@Autowired
	private TransOrderService transOrderService;

	@Autowired
	private TransApplyConditionService applyConditionService;




	public TransApply get(String id) {
		TransApply apply = super.get(id);
		List<TransApplyCondition>	conditionList = applyConditionService.getByApplyId(apply.getId());
		TransGoodsGroup goodsGroup = transGoodsGroupDao.get (apply.getGroupId ());
		apply.setTransGoodsGroup (goodsGroup);
		apply.setContiditonList(conditionList);
		return apply;
	}

	@Transactional(isolation = Isolation.REPEATABLE_READ,readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
	public TransApply getLock(String id){
		TransApply transApply =  new TransApply();
		transApply.setId(id);
		TransApply apply = dao.getLock(transApply);
		List<TransApplyCondition>	conditionList = applyConditionService.getByApplyId(apply.getId());
		TransGoodsGroup goodsGroup = transGoodsGroupDao.get (apply.getGroupId ());
		apply.setTransGoodsGroup (goodsGroup);
		apply.setContiditonList(conditionList);
		return apply;
	}
	
	public List<TransApply> findList(TransApply transApply) {
		return super.findList(transApply);
	}
	
	public Page<TransApply> findPage(Page<TransApply> page, TransApply transApply) {
		return super.findPage(page, transApply);
	}
	

	public void save(TransApply entity , List<TransApplyCondition> conditionList) throws ValidationException {
		super.save(entity);
		applyConditionService.deleteByApplyid(entity.getId());
		if(entity.getIfHoldGroupStatus().equals(EnumUtil.YesNO.YES.toString())){
			for (TransApplyCondition condition : conditionList ) {
				condition.setApplyId (entity.getId ());
				applyConditionService.save(condition);
			}
		}

	}


	

	public void delete(TransApply transApply) throws ValidationException {
		super.delete(transApply);
		applyConditionService.deleteByApplyid(transApply.getId());
	}

	/**
	 * 修改订货表剩余数量
	 *
	 * @param delNum 需要减的剩余数量
	 */
	public void updateTransApplyNowNum(String applyid,int delNum){
		dao.updateTransApplyNowNum(applyid,delNum);
	}

	/**
	 * 艺术品订货申请
	 * @param applyid 订货表id
	 * @param num  订货数量
	 * @param applyType  订货类型
	 * @param addressId 地址表id
	 * @throws ValidationException
	 */

	public void  applyPresaleArt(String userName,String applyid, int num, String applyType, String addressId) throws ValidationException {


		UserUserinfo currUser =  userUserinfoService.getByNameLock(userName);
		BigDecimal kuiSunMoney =  BigDecimal.ZERO;
		TransApply apply =  getLock(applyid);
		TransBuyLog transBuyLog = new TransBuyLog();
		if(apply == null){
			throw  new ValidationException("订货活动不存在!");
		}
		//个人购买次数限制
		transBuyLog.setUserName (userName);
		transBuyLog.setApplyId (applyid);
		if(transBuyLogService.findList (transBuyLog).size ()>0){
			throw new ValidationException("您已参加过此次订货活动,请勿重复参加");
		}
		//亏损限制
		if(EnumUtil.YesNO.YES.toString ().equals (apply.getIfKuisunStatus ())){
			TransGoodsKuiSun kuiSungoods = transGoodsService.getKuiSunMoneyByName (currUser.getUserName (),apply.getIfKuisunBegin (),apply.getIfKuisunEnd ());
			if(kuiSungoods != null){
				kuiSunMoney = kuiSungoods.getKuiSunMoney ().multiply (new BigDecimal (-1));
			}
			if(kuiSunMoney.compareTo (new BigDecimal (apply.getIfKuisunMoney ()))== -1){
				throw new ValidationException("订货需要的苏陕积分不足");
			}
		}
		//持有某票限制
		if(EnumUtil.YesNO.YES.toString ().equals (apply.getIfHoldGroupStatus ())){
			List<TransApplyCondition> list = apply.getContiditonList ();
			if(list != null) {
				for (TransApplyCondition transApplyCondition : list) {
					int holdGoodsNum  = transGoodsService.getHoldGoodsNum (transApplyCondition.getHoldGroupId (), currUser.getUserName (), EnumUtil.GoodsType.hold.toString ());

					if (holdGoodsNum < Integer.parseInt (transApplyCondition.getHoldGroupNum ())) {
						throw new ValidationException("订货该艺术品需持有的艺术品数量不足");
					}
				}
			}
		}
		UserAddress userAddress = null;
		if(applyType.equals (EnumUtil.TransApplyType.order.toString ())){
			 userAddress =  userAddressDao.get(addressId);
			if(userAddress == null || !userAddress.getUserName().equals(currUser.getUserName())){
				throw  new ValidationException("收货地址有误!");
			}
		}
		if(apply.getMaxnum() >0  &&  num > apply.getMaxnum()){
			throw  new ValidationException("不符合订货数量限制,每人最多购买: "+apply.getMaxnum()+" 件");
		}
		if	(Integer.parseInt (apply.getNowNum ()) < 1){
			throw  new ValidationException("商品已卖完!");
		}
 		BigDecimal allMoney = apply.getMoney().multiply(new BigDecimal(num));
//		if(applyType.equals (EnumUtil.TransApplyType.Normal.toString ())){
//			allMoney = allMoney.add(new BigDecimal(num));
//		}
		if(currUser.getMoney().compareTo(allMoney) < 0){
			throw  new ValidationException("用户余额不足!",1001);
		}
		userUserinfoService.updateUserMoney(currUser.getUserName (),allMoney.multiply (new BigDecimal (-1)),"订货艺术品成功,编号:"+apply.getGroupId(), EnumUtil.MoneyChangeType.transApply);

		this.updateTransApplyNowNum(applyid,num);

		if(applyType.equals (EnumUtil.TransApplyType.Normal.toString ())){
			for(int i=0;i<num;i++){
				TransGoods goods= new TransGoods();
				goods.setGroupId(apply.getGroupId());
				goods.setUserName(currUser.getUserName());
				goods.setStatus(EnumUtil.GoodsType.hold.toString());
				goods.setBuyMoney(apply.getMoney());
				goods.setSellMoney(new BigDecimal(0));
				goods.initGoodNo();
				transGoodsService.save(goods);
			}
			transBuyLogService.addTransLog(currUser.getUserName(), "", apply.getGroupId(),apply.getId (),num , apply.getMoney() ,"订货艺术品成功-类型[仓库代管]" , EnumUtil.TransBuyLogType.apply.toString () );
		}
		else if(applyType.equals(EnumUtil.TransApplyType.order.toString ())){

			for(int i=0;i<num;i++){
				TransGoods goods= new TransGoods();
				goods.setGroupId(apply.getGroupId());
				goods.setUserName(currUser.getUserName());
				goods.setStatus(EnumUtil.GoodsType.ordre.toString());
				goods.setBuyMoney(apply.getMoney());
				goods.setSellMoney(apply.getMoney());
				goods.initGoodNo();
				transGoodsService.save(goods);
			}
			TransOrder transOrder = new TransOrder();
			transOrder.setUserName(currUser.getUserName());
			transOrder.setGroupId(apply.getGroupId());
			transOrder.setGoodsName(apply.getTransGoodsGroup().getName());
			transOrder.setAddress(userAddress.getAddress());
			transOrder.setMobile(userAddress.getMobile());
			transOrder.setConsignee(userAddress.getTrueName());
			transOrder.setPostCode(userAddress.getPostcode());
			transOrder.setType(EnumUtil.CheckType.uncheck.toString());
			transOrder.setNum (num);
			transOrderService.save(transOrder);
			transBuyLogService.addTransLog(currUser.getUserName(), "", apply.getGroupId(),apply.getId (),num , apply.getMoney() ,"订货艺术品成功-类型[自主提货]" , EnumUtil.TransBuyLogType.apply.toString () );
		}

	}

	/**
	 * 后台配发艺术品
	 * @param transApply
	 */
    public void adminApplyTransGoods(AdminApplyTransGoods transApply) {
		transApply.validateModule();
		UserUserinfo currUser =  userUserinfoService.getByNameLock(transApply.getUserName());
		BigDecimal allMoney = (transApply.getMoney().multiply(new BigDecimal(transApply.getNum())).add(new BigDecimal(0)));
		if(currUser.getMoney().compareTo(allMoney) < 0){
			throw  new ValidationException("用户余额不足!");
		}
		userUserinfoService.updateUserMoney(currUser.getUserName (),allMoney.multiply (new BigDecimal (-1)),"配发艺术品成功,编号:"+transApply.getGroupId(), EnumUtil.MoneyChangeType.transApply);
		for(int i=0;i<transApply.getNum();i++){
			TransGoods goods= new TransGoods();
			goods.setGroupId(transApply.getGroupId());
			goods.setUserName(currUser.getUserName());
			goods.setStatus(EnumUtil.GoodsType.hold.toString());
			goods.setBuyMoney(transApply.getMoney());
			goods.setSellMoney(new BigDecimal(0));
			goods.initGoodNo();
			transGoodsService.save(goods);
		}
		transBuyLogService.addTransLog(currUser.getUserName(), "", transApply.getGroupId(),transApply.getId (),transApply.getNum() , transApply.getMoney() ,"手动配发艺术品" , EnumUtil.TransBuyLogType.apply.toString () );
    }
}