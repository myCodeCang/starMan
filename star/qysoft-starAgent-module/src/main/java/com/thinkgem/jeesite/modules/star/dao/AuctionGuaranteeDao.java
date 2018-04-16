/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.star.entity.AuctionGuarantee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 拍卖DAO接口
 * @author luo
 * @version 2017-09-25
 */
@MyBatisDao
public interface AuctionGuaranteeDao extends CrudDao<AuctionGuarantee> {

    AuctionGuarantee findByNameId(@Param("userName") String userName,@Param("auctionId")  String auctionId);

    void updateStatus(@Param("id")String id,@Param("status") String status);

    List<AuctionGuarantee> findByAuctionId(String auctionId);
}