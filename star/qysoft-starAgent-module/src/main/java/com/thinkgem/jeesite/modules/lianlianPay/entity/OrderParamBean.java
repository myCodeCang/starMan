package com.thinkgem.jeesite.modules.lianlianPay.entity;

public class OrderParamBean extends ApiBaseBean {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private String no_order; // 商家订单号
	private String oid_partner; // 商户号
	private String money_order; // 订单金额
	private String user_id; // 操作员号
	private String name_goods; // 商品名称
	private String dt_order; // 商家订单时间
	private String notify_url; // 商家通知地址
	private String info_order; // 订单描述
	private String memo2; // 商家预留
	private String pay_type; // 支付方式
	private String risk_item; // 风控参数
	private String channel_order; // 渠道单号
	private String oid_order; // 银+订单号
	private String dimension_url; // 二维码地址
	private String pay_status; // 支付状态
	private String desc_goods; // 订单描述
	private String dt_finish; // 订单完成时间
	private String barcode; // 条码信息
	private String oid_paybill; // 支付单号

	private String refer_no; // 检索参考号 v2.0版本
	private String card_no; // 银行卡号 v2.0版本
	private String pos_id; // pos序列号 v2.0版本

	private String terminal_id; // 上游终端号 v3.0版本
	private String tf_merchant_id; // 上游商户号 v3.0版本
	private String card_institute; // 发卡机构号 v3.0版本
	private String sys_trace_no; // 凭证号 v3.0版本
	private String batch_no; // 批次号 v3.0版本
	private String card_type; // 1信用卡，0借记卡 v3.0版本
	private String channel_code; // 交易渠道编号
	private String wallet_user_id; // 钱包用户id
	private String day_billvalid; // 订单有效期，单位-天
	private String oid_biz;
	private String appid;// 商家appid
	private String cust_ip;// h5支付申请主体ip;
	private String openid;//公众号用户id

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getCust_ip() {
		return cust_ip;
	}

	public void setCust_ip(String cust_ip) {
		this.cust_ip = cust_ip;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getDay_billvalid() {
		return day_billvalid;
	}

	public void setDay_billvalid(String dayBillvalid) {
		day_billvalid = dayBillvalid;
	}

	public String getWallet_user_id() {
		return wallet_user_id;
	}

	public void setWallet_user_id(String walletUserId) {
		wallet_user_id = walletUserId;
	}

	public String getOid_paybill() {
		return oid_paybill;
	}

	public void setOid_paybill(String oidPaybill) {
		oid_paybill = oidPaybill;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getChannel_code() {
		return channel_code;
	}

	public void setChannel_code(String channelCode) {
		channel_code = channelCode;
	}

	public String getDt_finish() {
		return dt_finish;
	}

	public void setDt_finish(String dtFinish) {
		dt_finish = dtFinish;
	}

	public String getDesc_goods() {
		return desc_goods;
	}

	public void setDesc_goods(String descGoods) {
		desc_goods = descGoods;
	}

	public String getChannel_order() {
		return channel_order;
	}

	public void setChannel_order(String channelOrder) {
		channel_order = channelOrder;
	}

	public String getOid_order() {
		return oid_order;
	}

	public void setOid_order(String oidOrder) {
		oid_order = oidOrder;
	}

	public String getDimension_url() {
		return dimension_url;
	}

	public void setDimension_url(String dimensionUrl) {
		dimension_url = dimensionUrl;
	}

	public String getPay_status() {
		return pay_status;
	}

	public void setPay_status(String payStatus) {
		pay_status = payStatus;
	}

	public String getNo_order() {
		return no_order;
	}

	public void setNo_order(String noOrder) {
		no_order = noOrder;
	}

	public String getOid_partner() {
		return oid_partner;
	}

	public void setOid_partner(String oidPartner) {
		oid_partner = oidPartner;
	}

	public String getMoney_order() {
		return money_order;
	}

	public void setMoney_order(String moneyOrder) {
		money_order = moneyOrder;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String userId) {
		user_id = userId;
	}

	public String getName_goods() {
		return name_goods;
	}

	public void setName_goods(String nameGoods) {
		name_goods = nameGoods;
	}

	public String getDt_order() {
		return dt_order;
	}

	public void setDt_order(String dtOrder) {
		dt_order = dtOrder;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notifyUrl) {
		notify_url = notifyUrl;
	}

	public String getInfo_order() {
		return info_order;
	}

	public void setInfo_order(String infoOrder) {
		info_order = infoOrder;
	}

	public String getMemo2() {
		return memo2;
	}

	public void setMemo2(String memo2) {
		this.memo2 = memo2;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String payType) {
		pay_type = payType;
	}

	public String getRisk_item() {
		return risk_item;
	}

	public void setRisk_item(String riskItem) {
		risk_item = riskItem;
	}

	public String getRefer_no() {
		return refer_no;
	}

	public void setRefer_no(String referNo) {
		refer_no = referNo;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String cardNo) {
		card_no = cardNo;
	}

	public String getPos_id() {
		return pos_id;
	}

	public void setPos_id(String posId) {
		pos_id = posId;
	}

	public String getTerminal_id() {
		return terminal_id;
	}

	public void setTerminal_id(String terminalId) {
		terminal_id = terminalId;
	}

	public String getTf_merchant_id() {
		return tf_merchant_id;
	}

	public void setTf_merchant_id(String tfMerchantId) {
		tf_merchant_id = tfMerchantId;
	}

	public String getCard_institute() {
		return card_institute;
	}

	public void setCard_institute(String cardInstitute) {
		card_institute = cardInstitute;
	}

	public String getSys_trace_no() {
		return sys_trace_no;
	}

	public void setSys_trace_no(String sysTraceNo) {
		sys_trace_no = sysTraceNo;
	}

	public String getBatch_no() {
		return batch_no;
	}

	public void setBatch_no(String batchNo) {
		batch_no = batchNo;
	}

	public String getCard_type() {
		return card_type;
	}

	public void setCard_type(String cardType) {
		card_type = cardType;
	}

	public String getOid_biz() {
		return oid_biz;
	}

	public void setOid_biz(String oid_biz) {
		this.oid_biz = oid_biz;
	}

}
