/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.trans.entity.TransBuyLog;
import com.thinkgem.jeesite.modules.trans.entity.TransStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 用户交易表DAO接口
 * @author xueyuliang
 * @version 2017-05-08
 */
@MyBatisDao
public interface TransBuyLogDao extends CrudDao<TransBuyLog> {
	public List<TransBuyLog> getChartDatasBuyGroup(@Param("groupId") String groupId);

	public List<TransStatistics> getTransStatistics(@Param("sql") String sql);

	public List<TransStatistics> getTransStatisticsMinute(@Param("groupId") String groupId, @Param("beginTime") String beginTime);

	//根据日期查询交易量
    public TransBuyLog getTransBuySumValue(TransBuyLog transBuyLog);
}