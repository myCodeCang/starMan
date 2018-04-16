/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.trans.entity.TransBuy;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 交易表DAO接口
 * @author luo
 * @version 2017-05-08
 */
@MyBatisDao
public interface TransBuyDao extends CrudDao<TransBuy> {


    /**
     * 分组查询应买单和应卖单列表
     * @param groupId
     * @param status
     * @param type
     * @param order
     * @param top
     * @return
     */
    public  List<TransBuy> getBuyGroupListLimit(@Param("groupId") String groupId, @Param("status") String status, @Param("type") String type, @Param("order") String order, @Param("top") Integer top);


    public List<TransBuy> findListLock(TransBuy transBuy);

    public void deleteSelling(TransBuy transBuy);
}