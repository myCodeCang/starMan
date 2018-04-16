/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.star.entity.EverydayHold;
import org.apache.ibatis.annotations.Param;

/**
 * 日均持仓DAO接口
 * @author xue
 * @version 2017-10-10
 */
@MyBatisDao
public interface EverydayHoldDao extends CrudDao<EverydayHold> {

    void updateNumById(@Param("id") String id, @Param("num") int num);
}