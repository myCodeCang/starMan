/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.user.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.user.dao.WorkProjectDao;
import com.thinkgem.jeesite.modules.user.entity.WorkProject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 项目Service
 * @author luo
 * @version 2017-08-04
 */
@Service
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class WorkProjectService extends CrudService<WorkProjectDao, WorkProject> {

	public WorkProject get(String id) {
		return super.get(id);
	}
	
	public List<WorkProject> findList(WorkProject workProject) {
		return super.findList(workProject);
	}
	
	public Page<WorkProject> findPage(Page<WorkProject> page, WorkProject workProject) {
		return super.findPage(page, workProject);
	}
	

	public void save(WorkProject workProject) {
		super.save(workProject);
	}
	

	public void delete(WorkProject workProject) {
		super.delete(workProject);
	}

	public void sellOutStatus(WorkProject workProject) {
		dao.sellOutStatus(workProject);
	}
	
}