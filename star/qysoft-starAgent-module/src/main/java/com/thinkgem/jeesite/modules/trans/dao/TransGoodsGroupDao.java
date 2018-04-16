/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.trans.entity.TransGoodsGroup;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 交易品表DAO接口
 * @author luojie
 * @version 2017-05-08
 */
@MyBatisDao
public interface TransGoodsGroupDao extends CrudDao<TransGoodsGroup> {
    public List<TransGoodsGroup> getTransListWithTrend(@Param("trendDate") Date trendDate, @Param("status") String status);

}