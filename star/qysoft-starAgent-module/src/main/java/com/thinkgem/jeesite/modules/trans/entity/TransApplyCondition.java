/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.entity;

import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import org.hibernate.validator.constraints.Length;

/**
 * 订货限制Entity
 * @author xueyuliang
 * @version 2017-05-09
 */
public class TransApplyCondition extends DataEntity<TransApplyCondition> {
	
	private static final long serialVersionUID = 1L;
	

	
	private String applyId;		// 订货表编号
	private String type;		// 1:  订货需要持有某票
	private String holdGroupId;		// 需要持有票的group id
	private String holdGroupNum;		// 需要持有票的数量
	private  String holdGroupName;   //需要持有票的name

	public String getHoldGroupName() {
		return holdGroupName;
	}

	public void setHoldGroupName(String holdGroupName) {
		this.holdGroupName = holdGroupName;
	}

	/**
	 * 验证模型字段
	 */
	public  void validateModule(boolean isInsert) throws ValidationException {
		// if(StringUtils2.isBlank(applyId)){
		// 	throw new ValidationException("订货表编号不能为空!");
		// }
		if(StringUtils2.isBlank(holdGroupId)){
			throw new ValidationException("需持有的票的ID!");
		}
		if(StringUtils2.isBlank(holdGroupNum)){
			throw new ValidationException("需持有的票的数量不能为空!");
		}
	}

	@Override
	public void preInsert() throws ValidationException {
		validateModule(true);
		super.preInsert();
	}

	@Override
	public void preUpdate() throws ValidationException {
		validateModule(false);
		super.preUpdate();
	}

	public TransApplyCondition() {
		super();
	}

	public TransApplyCondition(String id){
		super(id);
	}

	@Length(min=0, max=11, message="订货表编号长度必须介于 0 和 11 之间")
	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}
	
	@Length(min=0, max=1, message="1:  订货需要持有某票长度必须介于 0 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=11, message="需要持有票的group id长度必须介于 0 和 11 之间")
	public String getHoldGroupId() {
		return holdGroupId;
	}

	public void setHoldGroupId(String holdGroupId) {
		this.holdGroupId = holdGroupId;
	}
	
	@Length(min=0, max=11, message="需要持有票的数量长度必须介于 0 和 11 之间")
	public String getHoldGroupNum() {
		return holdGroupNum;
	}

	public void setHoldGroupNum(String holdGroupNum) {
		this.holdGroupNum = holdGroupNum;
	}
	
	

	
}