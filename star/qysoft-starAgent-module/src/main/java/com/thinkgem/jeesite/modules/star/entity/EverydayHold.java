/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.entity;

import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 日均持仓Entity
 * @author xue
 * @version 2017-10-10
 */
public class EverydayHold extends DataEntity<EverydayHold> {
	
	private static final long serialVersionUID = 1L;
	

	
	private String groupId;		// 产品代码
	private String userName;		// 用户名
	private BigDecimal money;		// 价格
	private int num = 0;		// 持有量
	private String isCheck;		// 是否出金

	//拓展字段
	private int type;  //类型
	private String parentName;  //推荐人
	private String trueName; //真实姓名
	private String userType;  //
	private Date createDateTimeBegin;
	private Date createDateTimeEnd;


	private Integer numBegin;  //持仓量筛选

	@Override
	protected void validateModule(boolean isInsert) throws ValidationException {
		super.validateModule(isInsert);
	}

	@Override
	public void preInsert() throws ValidationException {
		if(money == null){
			this.money = BigDecimal.ZERO;
		}
		if(StringUtils2.isBlank(isCheck)){
			this.isCheck = "0";
		}
		super.preInsert();
	}

	@Override
	public void preUpdate() throws ValidationException {
		super.preUpdate();
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public void setType(int type) {
		this.type = type;
	}

	public EverydayHold() {
		super();
	}

	public EverydayHold(String id){
		super(id);
	}

	@Length(min=1, max=11, message="产品代码长度必须介于 1 和 11 之间")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	@Length(min=0, max=100, message="用户名长度必须介于 0 和 100 之间")
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

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getType() {
		return type;
	}

	@Length(min=0, max=1, message="是否出金长度必须介于 0 和 1 之间")
	public String getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Date getCreateDateTimeBegin() {
		return createDateTimeBegin;
	}

	public void setCreateDateTimeBegin(Date createDateTimeBegin) {
		this.createDateTimeBegin = createDateTimeBegin;
	}

	public Date getCreateDateTimeEnd() {
		return createDateTimeEnd;
	}

	public void setCreateDateTimeEnd(Date createDateTimeEnd) {
		this.createDateTimeEnd = createDateTimeEnd;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public Integer getNumBegin() {
		return numBegin;
	}


	public void setNumBegin(Integer numBegin) {
		this.numBegin = numBegin;
	}
}