/**
/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.utils.VerifyUtils;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订货表Entity
 * @author xueyuliang
 * @version 2017-05-08
 */
public class TransApply extends DataEntity<TransApply> {
	@ExcelField(title="编号", align=2, sort=1,value="id")
	private static final long serialVersionUID = 1L;


	@ExcelField(title="标题", align=2, sort=2,value="title")
	private String title;		// 标题
	@ExcelField(title="艺术品编号", align=2, sort=3)
	private String groupId;		// ,关联交易品表主id
	@ExcelField(title="开始时间", align=2, sort=11)
	private Date startTime;		// 开始时间
	@ExcelField(title="结束时间", align=2, sort=12)
	private Date endTime;		// 结束时间
	@ExcelField(title="总订货数量", align=2, sort=5)
	private String allNum;		// 总数量
	@ExcelField(title="剩余订货数量", align=2, sort=6)
	private String nowNum;		// 剩余数量
	@ExcelField(title="状态", align=2, sort=7,dictType = "qy_start_end")
	private String status;		// 0: 结束 1:正常 默认1

	private String type;		// 1.符合条件人可见  2.所有人可见
	@ExcelField(title="描述", align=2, sort=10)
	private String message;		// 描述
	@ExcelField(title="亏损积分活动", align=2, sort=8,dictType = "qy_trans_switch")
	private String ifKuisunStatus ="0";		// 亏损积分优先抢购活动 0 :关闭  1:开启
	private String ifKuisunMoney = "0";		// 亏损积分大于等于某值可见
	private Date ifKuisunBegin;		// 亏损积分计算开始时间
	private Date ifKuisunEnd;		// 亏损积分计算开始时间
	@ExcelField(title="订货持票活动", align=2, sort=9,dictType = "qy_trans_switch")
	private String ifHoldGroupStatus ="0";		// 订货需要持有某票的开关  0:关闭  1:开启
	private int maxnum;		// 每个人最多订货几个 0代表无限制
	private BigDecimal  money; //订货金额


	//扩展字段
	@ExcelField(title="艺术品名称", align=2, sort=4,value="transGoodsGroup.name")
	private TransGoodsGroup transGoodsGroup;  //交易品组

	private List<TransApplyCondition> contiditonList ; //限制条件列表

	/**
	 * 验证模型字段
	 */
	public  void validateModule(boolean isInsert) throws ValidationException {
		if(StringUtils2.isBlank(title)){
			throw new ValidationException("标题不能为空!");
		}
		if(StringUtils2.isBlank(groupId)){
			throw  new ValidationException("商品编号不能为空!");
		}

		if(startTime == null||endTime == null){
			throw  new ValidationException("开始时间和添加时间不能为空!");
		}
		if(!VerifyUtils.isInteger (allNum)){
			throw  new ValidationException("请输入正确的总数量!");
		}
		if(!VerifyUtils.isInteger (nowNum)) {
			if(Integer.parseInt (nowNum)!=0){
				throw  new ValidationException("请输入正确的剩余数量!");
			}
		}
		if(maxnum<0){
			throw  new ValidationException("请输入正确的限制购买数量!");
		}
		if(Integer.parseInt (nowNum) > Integer.parseInt (allNum)){
			throw  new ValidationException("剩余数量必须小于等于总数量!");
		}
		if(money.compareTo (BigDecimal.ZERO)<0){
			throw  new ValidationException("请输入正确的订货金额!");
		}
	}

	@Override
	public void preUpdate() throws ValidationException {
		validateModule(false);
		super.preUpdate();

		//验证apply字段不可为空
		//qydo 当前数量不可小于总数量
		// ifKuisunStatus 等于1 则需要验证亏损字段是否合法
		//	ifHoldGroupStatus 等于1 则需要验证 condition的参数是否合法



	}

	@Override
	public void preInsert() throws ValidationException {

		this.setStatus("1");
		this.setType("2");

		//验证apply字段不可为空
		//qydo 当前数量不可小于总数量
		// ifKuisunStatus 等于1 则需要验证亏损字段是否合法
		//	ifHoldGroupStatus 等于1 则需要验证 condition的参数是否合法

		validateModule(true);
		super.preInsert();
	}

	public TransApply() {
		super();
	}

	public TransApply(String id){
		super(id);
	}

	@Length(min=0, max=255, message="标题长度必须介于 0 和 255 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=0, max=11, message="交易品组编号,关联交易品表主id长度必须介于 0 和 11 之间")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
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
	
	@Length(min=0, max=11, message="总数量长度必须介于 0 和 11 之间")
	public String getAllNum() {
		return allNum;
	}

	public void setAllNum(String allNum) {
		this.allNum = allNum;
	}
	
	@Length(min=0, max=11, message="剩余数量长度必须介于 0 和 11 之间")
	public String getNowNum() {
		return nowNum;
	}

	public void setNowNum(String nowNum) {
		this.nowNum = nowNum;
	}
	
	@Length(min=0, max=1, message="0: 结束 1:正常 默认1长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Length(min=0, max=1, message="0: 结束 1:正常 默认1长度必须介于 0 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=255, message="描述长度必须介于 0 和 255 之间")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@Length(min=0, max=1, message="亏损积分优先抢购活动 0 :关闭  1:开启长度必须介于 0 和 1 之间")
	public String getIfKuisunStatus() {
		return ifKuisunStatus;
	}

	public void setIfKuisunStatus(String ifKuisunStatus) {
		this.ifKuisunStatus = ifKuisunStatus;
	}
	
	@Length(min=0, max=11, message="亏损积分大于等于某值可见长度必须介于 0 和 11 之间")
	public String getIfKuisunMoney() {
		return ifKuisunMoney;
	}

	public void setIfKuisunMoney(String ifKuisunMoney) {
		this.ifKuisunMoney = ifKuisunMoney;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getIfKuisunBegin() {
		return ifKuisunBegin;
	}

	public void setIfKuisunBegin(Date ifKuisunBegin) {
		this.ifKuisunBegin = ifKuisunBegin;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getIfKuisunEnd() {
		return ifKuisunEnd;
	}

	public void setIfKuisunEnd(Date ifKuisunEnd) {
		this.ifKuisunEnd = ifKuisunEnd;
	}
	
	@Length(min=0, max=1, message="订货需要持有某票的开关  0:关闭  1:开启长度必须介于 0 和 1 之间")
	public String getIfHoldGroupStatus() {
		return ifHoldGroupStatus;
	}

	public void setIfHoldGroupStatus(String ifHoldGroupStatus) {
		this.ifHoldGroupStatus = ifHoldGroupStatus;
	}
	public int getMaxnum() {
		return maxnum;
	}

	public void setMaxnum(int maxnum) {
		this.maxnum = maxnum;
	}


    public TransGoodsGroup getTransGoodsGroup() {
        return transGoodsGroup;
    }

    public void setTransGoodsGroup(TransGoodsGroup transGoodsGroup) {
        this.transGoodsGroup = transGoodsGroup;
    }

	public List<TransApplyCondition> getContiditonList() {
		return contiditonList;
	}

	public void setContiditonList(List<TransApplyCondition> contiditonList) {
		this.contiditonList = contiditonList;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}
}