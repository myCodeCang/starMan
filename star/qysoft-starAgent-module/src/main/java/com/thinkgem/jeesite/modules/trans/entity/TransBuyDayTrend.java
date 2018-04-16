/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易历史价格表Entity
 * @author luo
 * @version 2017-05-10
 */
public class TransBuyDayTrend extends DataEntity<TransBuyDayTrend> {
	private static final long serialVersionUID = 1L;


	@ExcelField(title="艺术品编号", align=2, sort=1)
	private String groupId;		// 艺术品名称
	@ExcelField(title="当前价", align=2, sort=3)
	private BigDecimal nowMoney;		// 当前价
	@ExcelField(title="开盘价", align=2, sort=4)
	private BigDecimal startMoney;		// 开盘价
	@ExcelField(title="收盘价", align=2, sort=5)
	private BigDecimal endMoney;		// 收盘价
	@ExcelField(title="最高价", align=2, sort=6)
	private BigDecimal higMoney;		// 最高价
	@ExcelField(title="最低价", align=2, sort=7)
	private BigDecimal lowMoney;		// 最低价
	private BigDecimal amount;		// 交易量
	@ExcelField(title="创建时间", align=2, sort=8)
	private Date addDate;		// 创建时间
	@ExcelField(title="艺术品名称", align=2, sort=2)
	private String name;   //艺术品名称


	//扩展字段
	private  String createDateBegin; //查询时间范围
	private  String createDateEnd;

	/**
	 * 验证模型字段
	 */
	public  void validateModule(boolean isInsert) throws ValidationException {
		if(StringUtils2.isBlank(groupId)){
			throw new ValidationException("艺术品编号不能为空!");
		}
		if(nowMoney == null){
			throw new ValidationException("当前价不能为空!");
		}
		if(startMoney == null){
			throw new ValidationException("开盘价不能为空!");
		}
		if(endMoney == null){
			throw new ValidationException("收盘价不能为空!");
		}
		if(higMoney == null){
			throw new ValidationException("最高价不能为空!");
		}
		if(lowMoney == null){
			throw new ValidationException("最低价不能为空!");
		}

		if(addDate == null){
			throw new ValidationException("创建时间不能为空!");
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

	public TransBuyDayTrend() {
		super();
	}

	public TransBuyDayTrend(String id){
		super(id);
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=11, message="艺术品名称长度必须介于 0 和 11 之间")
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
	
	public BigDecimal getStartMoney() {
		return startMoney;
	}

	public void setStartMoney(BigDecimal startMoney) {
		this.startMoney = startMoney;
	}
	
	public BigDecimal getEndMoney() {
		return endMoney;
	}

	public void setEndMoney(BigDecimal endMoney) {
		this.endMoney = endMoney;
	}
	
	public BigDecimal getHigMoney() {
		return higMoney;
	}

	public void setHigMoney(BigDecimal higMoney) {
		this.higMoney = higMoney;
	}
	
	public BigDecimal getLowMoney() {
		return lowMoney;
	}

	public void setLowMoney(BigDecimal lowMoney) {
		this.lowMoney = lowMoney;
	}
	
	@Length(min=0, max=11, message="交易量长度必须介于 0 和 11 之间")
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}


	public String getCreateDateBegin() {
		return createDateBegin;
	}

	public void setCreateDateBegin(String createDateBegin) {
		this.createDateBegin = createDateBegin;
	}

	public String getCreateDateEnd() {
		return createDateEnd;
	}

	public void setCreateDateEnd(String createDateEnd) {
		this.createDateEnd = createDateEnd;
	}
}