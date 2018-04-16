/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;

/**
 * 拍卖Entity
 * @author luo
 * @version 2017-09-25
 */
public class AuctionGuarantee extends DataEntity<AuctionGuarantee> {
	
	private static final long serialVersionUID = 1L;
	

	
	private String auctionId;		// auction_id
	private String userName;		// user_name
	private BigDecimal money;		// money
	private String status;		// status
	
	public AuctionGuarantee() {
		super();
	}

	public AuctionGuarantee(String id){
		super(id);
	}

	@Length(min=0, max=11, message="auction_id长度必须介于 0 和 11 之间")
	public String getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(String auctionId) {
		this.auctionId = auctionId;
	}
	
	@Length(min=0, max=64, message="user_name长度必须介于 0 和 64 之间")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	@Length(min=0, max=1, message="status长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

	
}