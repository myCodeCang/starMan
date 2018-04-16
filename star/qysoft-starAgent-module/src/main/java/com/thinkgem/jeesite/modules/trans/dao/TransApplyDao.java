/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.trans.entity.TransApply;
import org.apache.ibatis.annotations.Param;

/**
 * 订货表DAO接口
 * @author xueyuliang
 * @version 2017-05-08
 */
@MyBatisDao
public interface TransApplyDao extends CrudDao<TransApply> {
    public void updateTransApplyNowNum(@Param("id") String id, @Param("num") int num);

    /**
     * 锁表获取记录
     */
    public TransApply getLock(TransApply apply);


}