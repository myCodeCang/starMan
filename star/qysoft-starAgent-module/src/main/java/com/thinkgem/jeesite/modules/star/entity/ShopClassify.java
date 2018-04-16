/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

/**
 * 商家分类Entity
 * @author luojie
 * @version 2017-07-14
 */
public class ShopClassify extends DataEntity<ShopClassify> {
	
	private static final long serialVersionUID = 1L;
	

	
	private String classifyname;		// 分类名称
	private String classifyimg;		// 分类图标
	private String isuser;		// 是否使用
	
	public ShopClassify() {
		super();
	}

	public ShopClassify(String id){
		super(id);
	}

	@Length(min=0, max=255, message="分类名称长度必须介于 0 和 255 之间")
	public String getClassifyname() {
		return classifyname;
	}

	public void setClassifyname(String classifyname) {
		this.classifyname = classifyname;
	}
	
	@Length(min=0, max=255, message="分类图标长度必须介于 0 和 255 之间")
	public String getClassifyimg() {
		return classifyimg;
	}

	public void setClassifyimg(String classifyimg) {
		this.classifyimg = classifyimg;
	}
	
	@Length(min=0, max=1, message="是否使用长度必须介于 0 和 1 之间")
	public String getIsuser() {
		return isuser;
	}

	public void setIsuser(String isuser) {
		this.isuser = isuser;
	}
	
	

	
}