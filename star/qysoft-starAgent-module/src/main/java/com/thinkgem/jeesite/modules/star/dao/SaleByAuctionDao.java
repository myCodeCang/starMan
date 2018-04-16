/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.star.entity.SaleByAuction;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 拍卖2DAO接口
 * @author luo
 * @version 2017-09-25
 */
@MyBatisDao
public interface SaleByAuctionDao extends CrudDao<SaleByAuction> {


    SaleByAuction getByLock(String id);

    void updateNowMoney(@Param("id") String id, @Param("nowMoney") BigDecimal nowMoney,@Param("succeedUser")  String succeedUser);

    void updateStatus(@Param("id")String id, @Param("status")String status);

    public SaleByAuction getByGroupId(String groupId);
}