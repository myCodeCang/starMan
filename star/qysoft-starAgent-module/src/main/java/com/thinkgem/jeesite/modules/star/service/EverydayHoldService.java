/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.modules.star.dao.BaseGoodsGroupDao;
import com.thinkgem.jeesite.modules.star.entity.BaseGoodsGroup;
import com.thinkgem.jeesite.modules.user.entity.UserReport;
import com.thinkgem.jeesite.modules.user.entity.UserUserinfo;
import com.thinkgem.jeesite.modules.user.service.UserUserinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.star.entity.EverydayHold;
import com.thinkgem.jeesite.modules.star.dao.EverydayHoldDao;

/**
 * 日均持仓Service
 * @author xue
 * @version 2017-10-10
 */
@Service
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class EverydayHoldService extends CrudService<EverydayHoldDao, EverydayHold> {
	@Autowired
	private UserUserinfoService userUserinfoService;
	@Autowired
	private BaseGoodsGroupDao baseGoodsGroupDao;
	public EverydayHold get(String id) {
		return super.get(id);
	}
	
	public List<EverydayHold> findList(EverydayHold everydayHold) {
		return super.findList(everydayHold);
	}
	
	public Page<EverydayHold> findPage(Page<EverydayHold> page, EverydayHold everydayHold) {
		return super.findPage(page, everydayHold);
	}
	

	public void save(EverydayHold everydayHold) {
		super.save(everydayHold);
	}
	

	public void delete(EverydayHold everydayHold) {
		super.delete(everydayHold);
	}

    public void updateDataByType(String userName, String groupId, int amount, BigDecimal money) {

			EverydayHold currHold = new EverydayHold();
			currHold.setUserName(userName);
			currHold.setMoney(money);
			currHold.setGroupId(groupId);
			currHold.setNum(amount);
			this.save(currHold);
    }
}