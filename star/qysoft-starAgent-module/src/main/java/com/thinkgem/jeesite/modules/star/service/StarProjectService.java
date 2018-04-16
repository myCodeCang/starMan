/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.star.dao.StarProjectDao;
import com.thinkgem.jeesite.modules.star.entity.StarProject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 明星产品Service
 * @author luo
 * @version 2017-10-18
 */
@Service
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class StarProjectService extends CrudService<StarProjectDao, StarProject> {

	public StarProject get(String id) {
		return super.get(id);
	}
	
	public List<StarProject> findList(StarProject starProject) {
		return super.findList(starProject);
	}
	
	public Page<StarProject> findPage(Page<StarProject> page, StarProject starProject) {
		return super.findPage(page, starProject);
	}
	

	public void save(StarProject starProject) {
		super.save(starProject);
	}
	

	public void delete(StarProject starProject) {
		super.delete(starProject);
	}
	
}