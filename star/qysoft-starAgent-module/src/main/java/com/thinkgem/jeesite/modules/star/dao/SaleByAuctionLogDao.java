/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.star.entity.SaleByAuctionLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 拍卖3DAO接口
 * @author luo
 * @version 2017-09-25
 */
@MyBatisDao
public interface SaleByAuctionLogDao extends CrudDao<SaleByAuctionLog> {

    List<SaleByAuctionLog> getByAuctionIdAfterId(@Param("auctionId") String auctionId,@Param("idAfter") String idAfter);

    List<SaleByAuctionLog> findByUser(String auctionUser);
}