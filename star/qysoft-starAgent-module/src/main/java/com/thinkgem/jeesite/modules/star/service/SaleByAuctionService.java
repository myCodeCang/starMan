/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.service;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.star.dao.SaleByAuctionDao;
import com.thinkgem.jeesite.modules.star.entity.AuctionGuarantee;
import com.thinkgem.jeesite.modules.star.entity.SaleByAuction;
import com.thinkgem.jeesite.modules.star.entity.SaleByAuctionLog;
import com.thinkgem.jeesite.modules.user.entity.UserUserinfo;
import com.thinkgem.jeesite.modules.user.service.UserUserinfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 拍卖2Service
 * @author luo
 * @version 2017-09-25
 */
@Service
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class SaleByAuctionService extends CrudService<SaleByAuctionDao, SaleByAuction> {

	@Resource
	private UserUserinfoService userUserinfoService;
	@Resource
	private SaleByAuctionLogService saleByAuctionLogService;
	@Resource
	private AuctionGuaranteeService auctionGuaranteeService;

	public SaleByAuction get(String id) {
		return super.get(id);
	}

	public SaleByAuction getByLock(String id) {
		return dao.getByLock(id);
	}

	public List<SaleByAuction> findList(SaleByAuction saleByAuction) {
		return super.findList(saleByAuction);
	}
	
	public Page<SaleByAuction> findPage(Page<SaleByAuction> page, SaleByAuction saleByAuction) {
		return super.findPage(page, saleByAuction);
	}
	

	public void save(SaleByAuction saleByAuction) {
		super.save(saleByAuction);
	}
	

	public void delete(SaleByAuction saleByAuction) {
		super.delete(saleByAuction);
	}

	private void updateNowMoney(String id, BigDecimal nowMoney, String succeedUser) {
		dao.updateNowMoney(id,nowMoney,succeedUser);
	}

	/**
	 * 拍卖竞价
	 * @param userName
	 * @param auctionId
	 * @param multiple
	 */
    public void makingAnOffer(String userName, String auctionId, String multiple)throws ValidationException {
		SaleByAuction saleByAuction = this.getByLock(auctionId);
		UserUserinfo userinfo = userUserinfoService.getByNameLock(userName);
		if(userinfo == null){
			throw new ValidationException("操作用户不存在!");
		}
		AuctionGuarantee guarantee = auctionGuaranteeService.findByNameId(userinfo.getId(), auctionId);
		if(saleByAuction == null){
			throw new ValidationException("需要竞拍的拍品不存在");
		}
		if(saleByAuction.getStatus().equals(EnumUtil.YesNO.YES.toString())){
			throw new ValidationException("拍卖已结束,下次再来吧!",1002);
		}
		if(!userinfo.getIsUsercenter().equals(EnumUtil.YesNO.YES.toString())){
			throw new ValidationException("您不是承销商,没有拍卖资格",1002);
		}
		if(StringUtils.isNotBlank(userinfo.getUsercenterAddress())){
			throw new ValidationException("您已经拍卖过产品了,不可重复拍卖",1002);
		}
		List<SaleByAuctionLog> auctionLogList = saleByAuctionLogService.findByUser(userinfo.getId());
		Optional<SaleByAuctionLog> logOptional = auctionLogList.stream().filter(p -> EnumUtil.YesNO.NO.toString().equals(p.getAuctionStatus())&&!p.getAuctionId().equals(auctionId)).findFirst();
		if(logOptional.isPresent()){
			throw new ValidationException("您正在参加其他拍卖,请等待拍结束后再来",1002);
		}
		if(guarantee == null||EnumUtil.YesNO.YES.toString().equals(guarantee.getStatus())){
			throw new ValidationException("您本次拍卖还未交保证金,请先去缴纳保证金",1003);
		}
		BigDecimal  nowMoney;
		try {
			nowMoney = new BigDecimal(multiple).multiply(saleByAuction.getNormMoney()).add(saleByAuction.getNowMoney());
		} catch (Exception e) {
			throw new ValidationException("竞拍价格错误");
		}

		//修改拍卖当前信息
		this.updateNowMoney(auctionId,nowMoney,userinfo.getId());
		//插入竞价记录
		SaleByAuctionLog saleByAuctionLog = new SaleByAuctionLog();
		saleByAuctionLog.setAuctionId(auctionId);
		saleByAuctionLog.setAuctionMoney(nowMoney);
		saleByAuctionLog.setAuctionUser(userinfo.getId());
		saleByAuctionLog.setStatus(EnumUtil.YesNO.YES.toString());
		saleByAuctionLogService.save(saleByAuctionLog);
	}

	public SaleByAuction getByGroupId(String groupId){
    	return dao.getByGroupId(groupId);
	}

	public void updateStatus(String id, String status) {
    	dao.updateStatus(id,status);
	}

	public void auctionSuccess(String id){
		SaleByAuction auction = this.getByLock(id);
		if(EnumUtil.YesNO.YES.toString().equals(auction.getStatus())){
			throw new ValidationException("该拍卖已结束");
		}
		this.updateStatus(id,EnumUtil.YesNO.YES.toString());
		List<AuctionGuarantee> auctionGuarantees = auctionGuaranteeService.findByAuctionId(id);
		List<AuctionGuarantee> auctionGuaranteeList = auctionGuarantees.stream().filter(p -> !p.getUserName().equals(auction.getSucceedUser())).collect(Collectors.toList());
		for(AuctionGuarantee guarantee:auctionGuaranteeList) {
			UserUserinfo user = userUserinfoService.get(guarantee.getUserName());
			//标记退还
			auctionGuaranteeService.updateStatus(guarantee.getId(), EnumUtil.YesNO.YES.toString());
			//保证金退换
			userUserinfoService.updateUserMoney(user.getUserName(), guarantee.getMoney(), "保证金退还,拍卖编号:" + guarantee.getAuctionId(), EnumUtil.MoneyChangeType.AUCTION_GUARANTEE_BACK);
		}
		UserUserinfo userUserinfo = userUserinfoService.get(auction.getSucceedUser());
		if(userUserinfo!=null){
			//插入承销商购买的票
			userUserinfoService.setUserCenter(userUserinfo.getUserName(),EnumUtil.YesNO.YES.toString(),"",auction.getGroupId());
		}
	}
}