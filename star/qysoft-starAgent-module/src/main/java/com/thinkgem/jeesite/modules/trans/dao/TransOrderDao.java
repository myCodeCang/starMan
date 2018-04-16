/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.trans.entity.TransOrder;

import java.util.List;

/**
 * 提货订单DAO接口
 * @author ss
 * @version 2017-05-08
 */
@MyBatisDao
public interface TransOrderDao extends CrudDao<TransOrder> {

    public void updateType(TransOrder transOrder);

    /**
     * 锁表查询
     * @param transOrder
     * @return
     */
    public List<TransOrder> findListLock(TransOrder transOrder);
}