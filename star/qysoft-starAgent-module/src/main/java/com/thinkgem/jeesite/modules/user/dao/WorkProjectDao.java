/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.user.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.user.entity.WorkProject;

/**
 * 项目DAO接口
 * @author luo
 * @version 2017-08-04
 */
@MyBatisDao
public interface WorkProjectDao extends CrudDao<WorkProject> {

    public void sellOutStatus(WorkProject workProject);
}