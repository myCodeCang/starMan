/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.trans.entity.TransApplyCondition;

import java.util.List;

/**
 * 订货限制DAO接口
 *
 * @author xueyuliang
 * @version 2017-05-09
 */
@MyBatisDao
public interface TransApplyConditionDao extends CrudDao<TransApplyCondition> {

    public List<TransApplyCondition> getByApplyId(String id);

    public void deleteByApplyid(String id);
}