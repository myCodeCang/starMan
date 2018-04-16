/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils2;
import com.thinkgem.jeesite.common.utils.VerifyUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 拍卖2Entity
 * @author luo
 * @version 2017-09-25
 */
public class SaleByAuction extends DataEntity<SaleByAuction> {
	
	private static final long serialVersionUID = 1L;
	

	
	private String groupId;		// 产品名称
	private String num;		// 数量
	private BigDecimal money;		// 初始价格
	private BigDecimal normMoney;		// 标准竞拍价
	private BigDecimal nowMoney;     //当前价格
	private Date startTime;		// 开始时间
	private Date endTime;		// 结束时间
	private String status;		// 状态
	private String succeedUser;		// 最后竞拍人
	private String detail;		// 详情
	private String classify; //分类


	//拓展字段
	private BaseGoodsGroup baseGoodsGroup;
	private Date endTimeAfter;
	@Override
	protected void validateModule(boolean isInsert) throws ValidationException {
		if(StringUtils.isBlank(groupId)){
			throw new ValidationException("产品名称不能为空");
		}
		if(!VerifyUtils.isInteger(num)){
			throw new ValidationException("数量格式错误");
		}
		if(DateUtils2.getSecondsOfTwoDate(endTime,startTime)>0){
			throw new ValidationException("时间添加错误");
		}

	}

	@Override
	public void preInsert() throws ValidationException {
		if(StringUtils.isBlank(this.status)){
			this.status="0";
		}
		if(this.nowMoney == null){
			this.nowMoney = this.money;
		}
		validateModule(true);
		super.preInsert();
	}

	@Override
	public void preUpdate() throws ValidationException {
		validateModule(false);
		super.preUpdate();
	}

	public String getClassify() {
		return classify;
	}

	public void setClassify(String classify) {
		this.classify = classify;
	}

	public Date getEndTimeAfter() {
		return endTimeAfter;
	}

	public void setEndTimeAfter(Date endTimeAfter) {
		this.endTimeAfter = endTimeAfter;
	}

	public SaleByAuction() {
		super();
	}

	public SaleByAuction(String id){
		super(id);
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public BigDecimal getNowMoney() {
		return nowMoney;
	}

	public void setNowMoney(BigDecimal nowMoney) {
		this.nowMoney = nowMoney;
	}

	public BaseGoodsGroup getBaseGoodsGroup() {
		return baseGoodsGroup;
	}

	public void setBaseGoodsGroup(BaseGoodsGroup baseGoodsGroup) {
		this.baseGoodsGroup = baseGoodsGroup;
	}

	@Length(min=0, max=11, message="数量长度必须介于 0 和 11 之间")
	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getNormMoney() {
		return normMoney;
	}

	public void setNormMoney(BigDecimal normMoney) {
		this.normMoney = normMoney;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Length(min=0, max=1, message="status长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=64, message="获拍人长度必须介于 0 和 64 之间")
	public String getSucceedUser() {
		return succeedUser;
	}

	public void setSucceedUser(String succeedUser) {
		this.succeedUser = succeedUser;
	}
	
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	

	
}