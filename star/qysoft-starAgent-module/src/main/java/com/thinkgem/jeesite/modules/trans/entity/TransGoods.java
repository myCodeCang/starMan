
/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易品持仓Entity
 * @author luo
 * @version 2017-05-10
 */
public class TransGoods extends DataEntity<TransGoods> {
	private static final long serialVersionUID = 1L;


	@ExcelField(title="艺术品编号", align=2, sort=3)
	private String groupId;		// 交易组编号
	@ExcelField(title="用户名", align=2, sort=5)
	private String userName;		// 用户名
	@ExcelField(title="状态", align=2, sort=7,dictType = "qy_trans_status")
	private String status;		// 状态
	@ExcelField(title="买入价格", align=2, sort=8)
	private BigDecimal buyMoney;		// 买入价格
	@ExcelField(title="出售价格", align=2, sort=9)
	private BigDecimal sellMoney;		// 出售价格
	private Date sellTime;		// 出售时间
	@ExcelField(title="真实姓名", align=2, sort=6)
	private String trueName;  //真实姓名
	@ExcelField(title="艺术品名称", align=2, sort=4)
	private String name; //艺术品名称
	@ExcelField(title="交易编号", align=2, sort=2)
	private String buyId ; //交易表id
	private String ischeck  ; //是否检测
	@ExcelField(title="商品编号", align=2, sort=1)
	private String goodsNo ; //商品编号
	private int transNum = 0; //商品交易次数
	private int num;

	// 扩展字段
	private Integer possessNum; //持有中
	@ExcelField(title="创建时间", align=2, sort=10,value="createDate")
	private Integer deputeNum;  //委托数
	private Integer sellNum;		//已出售
	private Integer takeGoodsNum; //已提货
	@ExcelField(title="出售时间", align=2, sort=11,value="sellTime")
	private Date beginSelltime;		// 开始 出售时间
	private Date endSelltime;		// 结束 出售时间
	private BigDecimal transactionPrice;		// 成交价
	private BigDecimal baseMoney;  //基础价格
	private String parentListLike;
	private String baseGoodsStatus;
	private String shopId; //会员号段
	private String[] groupIdArray;
	private String userRemark;//经纪人

	public String[] getGroupIdArray() {
		return groupIdArray;
	}

	public void setGroupIdArray(String[] groupIdArray) {
		this.groupIdArray = groupIdArray;
	}

	public String getUserRemark() {
		return userRemark;
	}

	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}


	public String getBaseGoodsStatus() {
		return baseGoodsStatus;
	}

	public void setBaseGoodsStatus(String baseGoodsStatus) {
		this.baseGoodsStatus = baseGoodsStatus;
	}

	/**
	 * 验证模型字段
	 */
	@Override
	public  void validateModule(boolean isInsert) throws ValidationException {

		if(StringUtils2.isBlank(groupId)){
			throw new ValidationException("艺术品编号不能为空!");
		}
		if(StringUtils2.isBlank(userName)){
			throw new ValidationException("用户名不能为空!");
		}
		if(StringUtils2.isBlank(status)){
			throw new ValidationException("状态不能为空!");
		}
		if(isInsert){
			if(num <=0){
				throw new ValidationException("数量必须大于0!");
			}
		}

//		 if(buyMoney == null){
//		 	throw new ValidationException("买入价格不能为空!");
//		 }
		 //如果售卖结束,必须要有出售价格
//		 if(status.equals(EnumUtil.GoodsType.Selled.toString())){
//			 if(sellMoney == null){
//				 throw new ValidationException("出售价格不能为空!");
//			 }
//			 if(sellTime == null){
//			  	throw new ValidationException("出售时间不能为空!");
//			 }
//		 }


	}

	@Override
	public void preInsert() throws ValidationException {
		this.sellMoney = BigDecimal.ZERO;
		validateModule(true);
		super.preInsert();

	}

	@Override
	public void preUpdate() throws ValidationException {
		validateModule(false);
		super.preUpdate();
	}


	/**
	 * 初始化商品编号
	 */
	public void initGoodNo(){
		String changeId = IdGen.uuid("trans_group_"+groupId);
		if(StringUtils2.isBlank(changeId) || changeId == "0"){
			throw new ValidationException("当前产品交易编号有误,请重新尝试");
		}else {
			setGoodsNo(groupId+changeId);
		}
	}

	public BigDecimal getBaseMoney() {
		return baseMoney;
	}

	public void setBaseMoney(BigDecimal baseMoney) {
		this.baseMoney = baseMoney;
	}

	public Date getBeginSelltime() {
		return beginSelltime;
	}

	public void setBeginSelltime(Date beginSelltime) {
		this.beginSelltime = beginSelltime;
	}

	public Date getEndSelltime() {
		return endSelltime;
	}

	public void setEndSelltime(Date endSelltime) {
		this.endSelltime = endSelltime;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public Integer getPossessNum() {
		return possessNum;
	}

	public void setPossessNum(Integer possessNum) {
		this.possessNum = possessNum;
	}

	public Integer getDeputeNum() {
		return deputeNum;
	}

	public void setDeputeNum(Integer deputeNum) {
		this.deputeNum = deputeNum;
	}

	public Integer getSellNum() {
		return sellNum;
	}

	public void setSellNum(Integer sellNum) {
		this.sellNum = sellNum;
	}

	public Integer getTakeGoodsNum() {
		return takeGoodsNum;
	}

	public void setTakeGoodsNum(Integer takeGoodsNum) {
		this.takeGoodsNum = takeGoodsNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TransGoods() {
		super();
	}

	public TransGoods(String id){
		super(id);
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	@Length(min=1, max=5, message="交易组编号长度必须介于 1 和 5 之间")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	@Length(min=1, max=255, message="用户名长度必须介于 1 和 255 之间")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Length(min=1, max=1, message="状态长度必须介于 1 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public BigDecimal getBuyMoney() {
		return buyMoney;
	}

	public void setBuyMoney(BigDecimal buyMoney) {
		this.buyMoney = buyMoney;
	}
	
	public BigDecimal getSellMoney() {
		return sellMoney;
	}

	public void setSellMoney(BigDecimal sellMoney) {
		this.sellMoney = sellMoney;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getSellTime() {
		return sellTime;
	}

	public void setSellTime(Date sellTime) {
		this.sellTime = sellTime;
	}


	public String getBuyId() {
		return buyId;
	}

	public void setBuyId(String buyId) {
		this.buyId = buyId;
	}

	public String getIscheck() {
		return ischeck;
	}

	public void setIscheck(String ischeck) {
		this.ischeck = ischeck;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public int getTransNum() {
		return transNum;
	}

	public void setTransNum(int transNum) {
		this.transNum = transNum;
	}

	public BigDecimal getTransactionPrice() {
		return transactionPrice;
	}

	public void setTransactionPrice(BigDecimal transactionPrice) {
		this.transactionPrice = transactionPrice;
	}

	public String getParentListLike() {
		return parentListLike;
	}

	public void setParentListLike(String parentListLike) {
		this.parentListLike = parentListLike;
	}
}