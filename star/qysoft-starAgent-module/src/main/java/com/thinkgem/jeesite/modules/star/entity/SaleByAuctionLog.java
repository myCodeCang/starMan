/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;

/**
 * 拍卖3Entity
 * @author luo
 * @version 2017-09-25
 */
public class SaleByAuctionLog extends DataEntity<SaleByAuctionLog> {
	
	private static final long serialVersionUID = 1L;
	

	
	private String auctionId;		// 拍卖编号
	private BigDecimal auctionMoney;		// auction_money
	private String status;		// status
	private String auctionUser;		// auction_user

	//拓展字段
	private String idAfter;
	private String auctionStatus;


	public String getAuctionStatus() {
		return auctionStatus;
	}

	public void setAuctionStatus(String auctionStatus) {
		this.auctionStatus = auctionStatus;
	}

	public String getIdAfter() {
		return idAfter;
	}

	public void setIdAfter(String idAfter) {
		this.idAfter = idAfter;
	}

	public SaleByAuctionLog() {
		super();
	}

	public SaleByAuctionLog(String id){
		super(id);
	}

	@Length(min=0, max=11, message="拍卖编号长度必须介于 0 和 11 之间")
	public String getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(String auctionId) {
		this.auctionId = auctionId;
	}

	public BigDecimal getAuctionMoney() {
		return auctionMoney;
	}

	public void setAuctionMoney(BigDecimal auctionMoney) {
		this.auctionMoney = auctionMoney;
	}

	@Length(min=0, max=1, message="status长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=64, message="auction_user长度必须介于 0 和 64 之间")
	public String getAuctionUser() {
		return auctionUser;
	}

	public void setAuctionUser(String auctionUser) {
		this.auctionUser = auctionUser;
	}
	
	

	
}