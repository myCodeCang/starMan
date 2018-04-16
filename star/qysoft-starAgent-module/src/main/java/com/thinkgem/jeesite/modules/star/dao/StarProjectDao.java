/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.star.entity.StarProject;

/**
 * 明星产品DAO接口
 * @author luo
 * @version 2017-10-18
 */
@MyBatisDao
public interface StarProjectDao extends CrudDao<StarProject> {
	
}