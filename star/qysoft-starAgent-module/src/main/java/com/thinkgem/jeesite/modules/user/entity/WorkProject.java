/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.user.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

/**
 * 项目Entity
 * @author luo
 * @version 2017-08-04
 */
public class WorkProject extends DataEntity<WorkProject> {
	
	private static final long serialVersionUID = 1L;
	

	
	private String name;		// 项目名称
	private String picture;		// 项目图片
	private String image1;			//轮播图1
	private String image2;			//轮播图2
	private String image3;			//轮播图3
	private String type;			//是否精选
	private BigDecimal money;		// 单价
	private String detail;		// 详情
	private String projectLevel;		// 项目等级
	private String message;		// 消息
	private String statue;		// 是否开启

	/**
	 * 扩展字段
	 */
	private String projectLevelName;
	
	public WorkProject() {
		super();
	}

	public WorkProject(String id){
		super(id);
	}

	@Length(min=0, max=255, message="项目名称长度必须介于 0 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=255, message="项目图片长度必须介于 0 和 255 之间")
	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	@Length(min=0, max=255, message="项目等级长度必须介于 0 和 255 之间")
	public String getProjectLevel() {
		return projectLevel;
	}

	public void setProjectLevel(String projectLevel) {
		this.projectLevel = projectLevel;
	}
	
	@Length(min=0, max=255, message="消息长度必须介于 0 和 255 之间")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@Length(min=0, max=1, message="是否开启长度必须介于 0 和 1 之间")
	public String getStatue() {
		return statue;
	}

	public void setStatue(String statue) {
		this.statue = statue;
	}

	public String getImage1() {
		return image1;
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}

	public String getImage3() {
		return image3;
	}

	public void setImage3(String image3) {
		this.image3 = image3;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProjectLevelName() {
		return projectLevelName;
	}

	public void setProjectLevelName(String projectLevelName) {
		this.projectLevelName = projectLevelName;
	}
}