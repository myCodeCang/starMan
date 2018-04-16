/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.entity;

import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户交易表Entity
 * @author xueyuliang
 * @version 2017-05-08
 */
public class TransBuyLog extends DataEntity<TransBuyLog> {
	
	private static final long serialVersionUID = 1L;


	@ExcelField(title="交易编号", align=2, sort=4)
	private String userName;		// 用户名
	@ExcelField(title="交易对方用户名", align=2, sort=6)
	private String transUserName;		// 交易对方用户名
	@ExcelField(title="艺术品编号", align=2, sort=2)
	private String groupId;		// 交易品组编号,关联交易品表主id
	private String applyId;       //认购活动的编号
	@ExcelField(title="交易数量", align=2, sort=8)
	private int num;		// 交易数量
	@ExcelField(title="交易单价", align=2, sort=9)
	private BigDecimal money;		// 单价金额
	@ExcelField(title="描述信息", align=2, sort=11)
	private String message;		// 描述信息
	@ExcelField(title="类型", align=2, sort=10,dictType = "trans_type")
	private String type;		// 类型 1:出售  2:买入 3:订货
	@ExcelField(title="真实姓名", align=2, sort=5)
	private String trueName;    //真实姓名
	@ExcelField(title="艺术品名称", align=2, sort=3)
	private String goodsName;
	@ExcelField(title="交易对方真实姓名", align=2, sort=7)
	private String transUserTrueName;
	@ExcelField(title="交易编号", align=2, sort=1)
	private String buyId ; //交易编号
	@ExcelField(title="创建时间", align=2, sort=12,value="createDate")
	private String isCheck ;  //是否统计
	private BigDecimal profit;		//盈利金额


	//查询字段
	private Date createDateBegin;
	private Date createDateEnd;

	private Date createDateTimeBegin;
	private Date createDateTimeEnd;

	/**
	 * 验证模型字段
	 */
	public  void validateModule(boolean isInsert) throws ValidationException {
//		if(StringUtils2.isBlank(userName)){
//			throw new ValidationException("用户名不能为空!");
//		}

		if(StringUtils2.isBlank(groupId)){
			throw new ValidationException("艺术品编号不能为空!");
		}
		if(StringUtils2.isBlank(String.valueOf(num))){
			throw new ValidationException("交易数量不能为空!");
		}
//		if(money == null){
//			throw new ValidationException("单价不能为空!");
//		}
//		if(StringUtils2.isBlank(type)){
//			throw new ValidationException("类型不能为空!");
//		}
	}


	@Override
	public void preInsert() throws ValidationException {
		validateModule(true);

		this.isCheck = "0";
		super.preInsert();
	}

	@Override
	public void preUpdate() throws ValidationException {
		validateModule(false);
		super.preUpdate();
	}



	public String getTransUserTrueName() {
		return transUserTrueName;
	}

	public void setTransUserTrueName(String transUserTrueName) {
		this.transUserTrueName = transUserTrueName;
	}



	//扩展参数
	private Date addTimeBegin;		// 时间内筛选

	private BigDecimal moneyBegin ;  //金钱大于等于

	private  String[] typeArray ; //类型包含


	public String getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}

	public BigDecimal getProfit() {
		return profit;
	}

	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public TransBuyLog() {
		super();
	}

	public TransBuyLog(String id){
		super(id);
	}

	@Length(min=0, max=255, message="用户名长度必须介于 0 和 255 之间")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Length(min=0, max=255, message="交易对方用户名长度必须介于 0 和 255 之间")
	public String getTransUserName() {
		return transUserName;
	}

	public void setTransUserName(String transUserName) {
		this.transUserName = transUserName;
	}
	
	@Length(min=0, max=11, message="交易品组编号,关联交易品表主id长度必须介于 0 和 11 之间")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	@Length(min=0, max=255, message="交易数量长度必须介于 0 和 255 之间")
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	
	@Length(min=0, max=255, message="描述信息长度必须介于 0 和 255 之间")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@Length(min=0, max=1, message="类型 1:出售  2:买入 3:订货长度必须介于 0 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public Date getAddTimeBegin() {
		return addTimeBegin;
	}

	public void setAddTimeBegin(Date addTimeBegin) {
		this.addTimeBegin = addTimeBegin;
	}

	public BigDecimal getMoneyBegin() {
		return moneyBegin;
	}

	public void setMoneyBegin(BigDecimal moneyBegin) {
		this.moneyBegin = moneyBegin;
	}


	public String[] getTypeArray() {
		return typeArray;
	}

	public void setTypeArray(String[] typeArray) {
		this.typeArray = typeArray;
	}

	public String getBuyId() {
		return buyId;
	}

	public void setBuyId(String buyId) {
		this.buyId = buyId;
	}

	public Date getCreateDateBegin() {
		return createDateBegin;
	}

	public void setCreateDateBegin(Date createDateBegin) {
		this.createDateBegin = createDateBegin;
	}

	public Date getCreateDateEnd() {
		return createDateEnd;
	}

	public void setCreateDateEnd(Date createDateEnd) {
		this.createDateEnd = createDateEnd;
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
}