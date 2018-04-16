/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.trans.entity.TransBuyDayTrend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 交易历史价格表DAO接口
 * @author luo
 * @version 2017-05-10
 */
@MyBatisDao
public interface TransBuyDayTrendDao extends CrudDao<TransBuyDayTrend> {
    public List<TransBuyDayTrend> getChartDatasBuyGroup(@Param("groupId") String groupId);
}