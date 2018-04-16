/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户交易亏损模型
 * @author luo
 * @version 2017-05-10
 */
public class TransGoodsKuiSun extends DataEntity<TransGoodsKuiSun> {

	private static final long serialVersionUID = 1L;


	@ExcelField(title="用户名", align=2, sort=1)
	private String userName;
	@ExcelField(title="真实姓名", align=2, sort=2)
	private String  trueName;
	@ExcelField(title="买入艺术品价格总计", align=2, sort=3)
	private BigDecimal buyMoney;
	@ExcelField(title="出售艺术品价格总计", align=2, sort=4)
	private BigDecimal sellMoney;
	@ExcelField(title="亏损金额", align=2, sort=5)
	private BigDecimal kuiSunMoney;

	//查询条件
	private Date beginSelltime;		// 开始 出售时间
	private Date endSelltime;		// 结束 出售时间
	private int order;				//亏损排序 0 盈利排序 1



	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
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

	public BigDecimal getKuiSunMoney() {
		return kuiSunMoney;
	}

	public void setKuiSunMoney(BigDecimal kuiSunMoney) {
		this.kuiSunMoney = kuiSunMoney;
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

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
}