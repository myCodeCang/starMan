package com.thinkgem.jeesite.modules.md.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.md.entity.MdTradeMain;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by kevin on 2017/10/2.
 */
@MyBatisDao
public interface MdTradeMainDao extends CrudDao<MdTradeMain> {
    MdTradeMain getNowMoney(String groupId);

    List<MdTradeMain> sumTransDayGroupByGroupId();

    MdTradeMain getPublishByGroupId(String groupId);

    void updateIsOverTop(@Param("id") String id,@Param("isOverTop") String isOverTop);

    void updateClosingPrice(@Param("closePrice") BigDecimal closePrice,@Param("createDate") Date createDate, @Param("groupId") String groupId);
}
