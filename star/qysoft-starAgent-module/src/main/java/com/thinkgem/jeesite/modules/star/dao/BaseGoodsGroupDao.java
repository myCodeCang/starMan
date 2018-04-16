/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.star.entity.BaseGoodsGroup;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 基础商品DAO接口
 * @author luojie
 * @version 2017-09-25
 */
@MyBatisDao
public interface BaseGoodsGroupDao extends CrudDao<BaseGoodsGroup> {

    void updateStatusTime(@Param("id") String id,@Param("status") String status, @Param("transStartTime")Date transStartTime);

    List<BaseGoodsGroup> findTradeList(BaseGoodsGroup baseGoodsGroup);

    void updateBaseMoney(@Param("id") String id, @Param("money") BigDecimal money);

    void updateBaseNum(@Param("id") String groupId, @Param("num") int num);
}