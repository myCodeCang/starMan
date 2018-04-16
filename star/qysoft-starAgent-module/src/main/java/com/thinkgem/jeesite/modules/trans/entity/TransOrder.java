/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.entity;

import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 提货订单Entity
 * @author ss
 * @version 2017-05-08
 */
public class TransOrder extends DataEntity<TransOrder> {
	@ExcelField(title="编号", align=2, sort=1,value="id")
	private static final long serialVersionUID = 1L;

	@ExcelField(title="用户名", align=2, sort=2)
	private String userName;		// 用户名
	@ExcelField(title="艺术品编号", align=2, sort=3)
	private String groupId;		// 商品编号
	@ExcelField(title="艺术品名称", align=2, sort=4)
	private String goodsName;   // 商品名称
	@ExcelField(title="收货地址", align=2, sort=8)
	private String address;		// 收货地址
	@ExcelField(title="手机号", align=2, sort=6)
	private String mobile;		// 手机号
	@ExcelField(title="收货人姓名", align=2, sort=7)
	private String consignee;		// 收货人姓名
	@ExcelField(title="邮编", align=2, sort=9)
	private String postCode;		// 邮编
	@ExcelField(title="提货数量", align=2, sort=5)
	private int num;		// 数量
	@ExcelField(title="状态", align=2, sort=10,dictType = "qy_shoporder_type")
	private String type;		// 类型

	/**
	 * 验证模型字段
	 */
	public  void validateModule(boolean isInsert) throws ValidationException {
		if(StringUtils2.isBlank(userName)){
			throw new ValidationException("用户名不能为空!");
		}
		if(StringUtils2.isBlank(groupId)){
			throw new ValidationException("艺术品编号不能为空!");
		}
		if(StringUtils2.isBlank(goodsName)){
			throw new ValidationException("商品名称不能为空!");
		}
		if(mobile == null){
			throw new ValidationException("手机号不能为空!");
		}
		if(StringUtils2.isBlank(consignee)){
			throw new ValidationException("收货人姓名不能为空!");
		}
	

	}

	@Override
	public void preUpdate() throws ValidationException {
		validateModule(true);
		super.preUpdate();

	}

	@Override
	public void preInsert() throws ValidationException {
		validateModule(false);
		super.preInsert();
		//qydo 新增效验, 商品必须存在,  所有参数不为空. 默认状态为1 , 待发货

	}



	//qydo 额外属性字段需要加注释 , 注意命名规则
	private Date addTimeBegin;		// 时间内筛选

	public Date getAddTimeBegin() {
		return addTimeBegin;
	}

	public void setAddTimeBegin(Date addTimeBegin) {
		this.addTimeBegin = addTimeBegin;
	}


	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public TransOrder() {
		super();
	}

	public TransOrder(String id){
		super(id);
	}

	@Length(min=1, max=255, message="用户名长度必须介于 1 和 255 之间")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Length(min=1, max=255, message="收货地址长度必须介于 1 和 255 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Length(min=1, max=255, message="手机长度必须介于 1 和 255 之间")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	@Length(min=1, max=255, message="邮编长度必须介于 1 和 255 之间")
	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	@Length(min=1, max=1, message="类型长度必须介于 1 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Length(min=1, max=255, message="类型长度必须介于 1 和 255 之间")
	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	@Length(min=1, max=255, message="类型长度必须介于 1 和 255 之间")
	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}


}