/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.entity;

import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import org.hibernate.validator.constraints.Length;

/**
 * 明星产品Entity
 * @author luo
 * @version 2017-10-18
 */
public class StarProject extends DataEntity<StarProject> {
	
	private static final long serialVersionUID = 1L;
	

	private String name;        //产品名称
	private String groupId;		// group_id
	private String status;		// 1上架 2 下架
	private String image;		// 大图
	private int time;   //所需时间
	private String detail;  //描述

	/**
	 * 验证模型字段
	 */
	@Override
	public  void validateModule(boolean isInsert) throws ValidationException {

		if (StringUtils2.isBlank(name)) {
			throw new ValidationException("产品名称不能为空!");
		}
		if (StringUtils2.isBlank(status)) {
			throw new ValidationException("状态不能为空!");
		}
		if (StringUtils2.isBlank(image)) {
			throw new ValidationException("图片不能为空!");
		}
		if (StringUtils2.isBlank(detail)) {
			throw new ValidationException("产品介绍不能为空!");
		}
	}

	//扩展字段
	private String starName; //明星名称
	
	public StarProject() {
		super();
	}

	public StarProject(String id){
		super(id);
	}

	@Length(min=0, max=11, message="group_id长度必须介于 0 和 11 之间")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	@Length(min=0, max=1, message="1上架 2 下架长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=255, message="大图长度必须介于 0 和 255 之间")
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStarName() {
		return starName;
	}

	public void setStarName(String starName) {
		this.starName = starName;
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
}