/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 收藏表Entity
 * @author luojie
 * @version 2017-09-28
 */
public class UserColection extends DataEntity<UserColection> {
	
	private static final long serialVersionUID = 1L;
	

	
	private String userName;		// user_name
	private String groupId;		// group_id
	private String message;		// message
	
	public UserColection() {
		super();
	}

	public UserColection(String id){
		super(id);
	}

	@Length(min=0, max=255, message="user_name长度必须介于 0 和 255 之间")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Length(min=0, max=11, message="group_id长度必须介于 0 和 11 之间")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	@Length(min=0, max=255, message="message长度必须介于 0 和 255 之间")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}




}