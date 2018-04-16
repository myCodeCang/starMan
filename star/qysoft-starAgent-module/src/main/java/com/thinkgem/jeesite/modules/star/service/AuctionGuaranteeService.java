/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.service;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.star.dao.AuctionGuaranteeDao;
import com.thinkgem.jeesite.modules.star.dao.SaleByAuctionDao;
import com.thinkgem.jeesite.modules.star.entity.AuctionGuarantee;
import com.thinkgem.jeesite.modules.star.entity.SaleByAuction;
import com.thinkgem.jeesite.modules.user.entity.UserUserinfo;
import com.thinkgem.jeesite.modules.user.service.UserUserinfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 拍卖Service
 * @author luo
 * @version 2017-09-25
 */
@Service
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class AuctionGuaranteeService extends CrudService<AuctionGuaranteeDao, AuctionGuarantee> {
	@Resource
	private SaleByAuctionDao saleByAuctionDao;
	@Resource
	private UserUserinfoService userUserinfoService;

	public AuctionGuarantee get(String id) {
		return super.get(id);
	}
	
	public List<AuctionGuarantee> findList(AuctionGuarantee auctionGuarantee) {
		return super.findList(auctionGuarantee);
	}
	
	public Page<AuctionGuarantee> findPage(Page<AuctionGuarantee> page, AuctionGuarantee auctionGuarantee) {
		return super.findPage(page, auctionGuarantee);
	}
	

	public void save(AuctionGuarantee auctionGuarantee) {
		super.save(auctionGuarantee);
	}
	

	public void delete(AuctionGuarantee auctionGuarantee) {
		super.delete(auctionGuarantee);
	}


	public AuctionGuarantee findByNameId(String userName, String auctionId) {
		return dao.findByNameId(userName,auctionId);
	}

    public void payAuctionMoney(String userName,String auctionId) throws VirtualMachineError{
		SaleByAuction auction = saleByAuctionDao.get(auctionId);
		UserUserinfo userinfo = userUserinfoService.getByNameLock(userName);
		if(userinfo == null){
			throw new ValidationException("用户不存在");
		}
		if(!EnumUtil.YesNO.YES.toString().equals(userinfo.getIsUsercenter())){
			throw new ValidationException("您不是承销商,没有资格参与拍卖,无需交付担保金!!");
		}
		AuctionGuarantee guarantee = this.findByNameId(userinfo.getId(), auctionId);
		if(StringUtils.isNotBlank(userinfo.getUsercenterAddress())){
			throw new ValidationException("您已拍卖过其他产品!!");
		}
		if(guarantee == null){
			guarantee = new AuctionGuarantee();
		}
		if(auction == null){
			throw new ValidationException("所要交付担保金的拍卖场次不存在!",1002);
		}
		if(auction.getStatus().equals(EnumUtil.YesNO.YES.toString())){
			throw new ValidationException("所要交付担保金的拍卖场次已结束!",1002);
		}
		if(guarantee != null && EnumUtil.YesNO.NO.toString().equals(guarantee.getStatus())){
			throw new ValidationException("本次拍卖您已交过保证金,请勿重复提交",1002);
		}

		BigDecimal money;
		try {
			money = new BigDecimal(Global.getOption("system_trans","auction_money","1000"));
		} catch (Exception e) {
			throw new ValidationException("拍卖保证金配置错误!");
		}
		if(userinfo.getMoney().compareTo(money)<0){
			throw new ValidationException("余额不足,请去充值",1001);
		}
		//扣除用户余额
		userUserinfoService.updateUserMoney(userName,money.multiply(BigDecimal.valueOf(-1)),"交付拍卖保证金,拍卖场次:"+auctionId, EnumUtil.MoneyChangeType.AUCTION_GUARANTEE);
		//插入记录
		guarantee.setUserName(userinfo.getId());
		guarantee.setStatus(EnumUtil.YesNO.NO.toString());
		guarantee.setAuctionId(auctionId);
		guarantee.setMoney(money);
		guarantee.setCreateDate(new Date());
		this.save(guarantee);

	}

    public void updateStatus(String id, String status) {
		dao.updateStatus(id,status);
    }

    public List<AuctionGuarantee> findByAuctionId(String auctionId) {
		return dao.findByAuctionId(auctionId);
    }

}