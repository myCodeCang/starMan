/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 保存实时价格Entity
 * @author 学
 * @version 2017-11-01
 */
public class RealTimePrice extends DataEntity<RealTimePrice> {
	
	private static final long serialVersionUID = 1L;
	

	
	private String groupId;		// group_id
	private BigDecimal price;		// price
	private String timeStamp; //时间戳

	//拓展字段
	private Date createDateBegin;
	private Date createDateEnd;

	public Date getCreateDateBegin() {
		return createDateBegin;
	}

	public void setCreateDateBegin(Date createDateBegin) {
		this.createDateBegin = createDateBegin;
	}

	public Date getCreateDateEnd() {
		return createDateEnd;
	}

	public void setCreateDateEnd(Date createDateEnd) {
		this.createDateEnd = createDateEnd;
	}

	public RealTimePrice() {
		super();
	}

	public RealTimePrice(String id){
		super(id);
	}

	@Length(min=0, max=11, message="group_id长度必须介于 0 和 11 之间")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
}