/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.entity;

import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.modules.md.entity.MdTradeMain;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 基础商品Entity
 * @author luojie
 * @version 2017-09-25
 */
public class BaseGoodsGroup extends DataEntity<BaseGoodsGroup> {
	
	private static final long serialVersionUID = 1L;
	

	
	private String categoryId;		// 所属分类
	private String name;		// 姓名
	private String description;		// 描述
	private String detail;		// 详情
	private int num;		// 数量
	private String thumb;		// 缩略图
	private String image;		// 大图
	private BigDecimal money;		// 起始价格
	private String isShow;		// 是否首页展示
	private String status;		// 状态
	private String starProductName;		// 明星产品姓名
	private String starProductDetail;		// 明星产品简介
	private int starTime;		// 所需时长
	private Date transStartTime;  //交易开始时间
	private String proImg; //产品图
	private String transRules; //交易规则

	//扩展字段
	private MdTradeMain mdTradeMain;
	private String attornFind; //持仓查询
	private int sum;

	@Override
	protected void validateModule(boolean isInsert) throws ValidationException {
		if(StringUtils2.isBlank(thumb)){
			throw new ValidationException("缩略图不能为空!");
		}
		if(StringUtils2.isBlank(image)){
			throw new ValidationException("明星图不能为空!");
		}
	}

	@Override
	public void preInsert() throws ValidationException {
		if(StringUtils2.isBlank(this.status)){
			this.status = "0";
		}
		validateModule(true);
		super.preInsert();
	}

	@Override
	public void preUpdate() throws ValidationException {
		validateModule(false);
		super.preUpdate();
	}

	public BaseGoodsGroup() {
		super();
	}

	public BaseGoodsGroup(String id){
		super(id);
	}

	@Length(min=0, max=11, message="所属分类长度必须介于 0 和 11 之间")
	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
	@Length(min=0, max=255, message="姓名长度必须介于 0 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=255, message="描述长度必须介于 0 和 255 之间")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getProImg() {
		return proImg;
	}

	public void setProImg(String proImg) {
		this.proImg = proImg;
	}

	@Length(min=0, max=255, message="缩略图长度必须介于 0 和 255 之间")
	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	
	@Length(min=0, max=255, message="大图长度必须介于 0 和 255 之间")
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	
	@Length(min=0, max=1, message="是否首页展示长度必须介于 0 和 1 之间")
	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	
	@Length(min=0, max=1, message="状态长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=255, message="明星产品姓名长度必须介于 0 和 255 之间")
	public String getStarProductName() {
		return starProductName;
	}

	public void setStarProductName(String starProductName) {
		this.starProductName = starProductName;
	}
	
	@Length(min=0, max=255, message="明星产品简介长度必须介于 0 和 255 之间")
	public String getStarProductDetail() {
		return starProductDetail;
	}

	public void setStarProductDetail(String starProductDetail) {
		this.starProductDetail = starProductDetail;
	}

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

	public int getStarTime() {
		return starTime;
	}

	public void setStarTime(int starTime) {
		this.starTime = starTime;
	}

	public Date getTransStartTime() {
		return transStartTime;
	}

	public void setTransStartTime(Date transStartTime) {
		this.transStartTime = transStartTime;
	}

	public MdTradeMain getMdTradeMain() {
		return mdTradeMain;
	}

	public void setMdTradeMain(MdTradeMain mdTradeMain) {
		this.mdTradeMain = mdTradeMain;
	}

	public String getTransRules() {
		return transRules;
	}

	public void setTransRules(String transRules) {
		this.transRules = transRules;
	}

	public String getAttornFind() {
		return attornFind;
	}

	public void setAttornFind(String attornFind) {
		this.attornFind = attornFind;
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}
}