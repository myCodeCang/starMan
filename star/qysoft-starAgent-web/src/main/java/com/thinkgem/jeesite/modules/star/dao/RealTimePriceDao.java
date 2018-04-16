/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.star.entity.RealTimePrice;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 保存实时价格DAO接口
 * @author 学
 * @version 2017-11-01
 */
@MyBatisDao
public interface RealTimePriceDao extends CrudDao<RealTimePrice> {

    RealTimePrice getNowPrice(String groupId);

    List<RealTimePrice> getRealTimePrice();

    RealTimePrice findLastMinutesList(@Param("groupId") String id, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

}