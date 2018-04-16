/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.service;

import java.util.List;

import com.thinkgem.jeesite.modules.star.dao.UserColectionDao;
import com.thinkgem.jeesite.modules.star.entity.UserColection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;

/**
 * 收藏表Service
 * @author luojie
 * @version 2017-09-28
 */
@Service
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class UserColectionService extends CrudService<UserColectionDao, UserColection> {

	public UserColection get(String id) {
		return super.get(id);
	}
	
	public List<UserColection> findList(UserColection userColection) {
		return super.findList(userColection);
	}
	
	public Page<UserColection> findPage(Page<UserColection> page, UserColection userColection) {
		return super.findPage(page, userColection);
	}
	

	public void save(UserColection userColection) {
		super.save(userColection);
	}
	

	public void delete(UserColection userColection) {
		super.delete(userColection);
	}

	public List<UserColection> findByUserName(String userName) {
	 return	dao.findByUserName(userName);
	}

	public UserColection getByNameAndId(String loginName, String groupId) {
		return dao.getByNameAndId(loginName,groupId);
	}

    public void cancelColection(String userName, String groupId) {
		dao.cancelColection(userName,groupId);
    }
}