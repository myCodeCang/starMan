package com.thinkgem.jeesite.modules.md.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.md.entity.MdTrade;
import com.thinkgem.jeesite.modules.md.entity.TradeState;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by kevin on 2017/10/2.
 */
@MyBatisDao
public interface MdTradeDao extends CrudDao<MdTrade>  {
    MdTrade getLock(String id);

    List<MdTrade> getBuyGroupListLimit(@Param("groupId") String groupId, @Param("status") String status, @Param("type") String type, @Param("order") String order, @Param("top") int top,@Param("createDate") Date createDate);

    void updateTradeState(@Param("id") int id, @Param("state") int state);

    void updateRemainNum(@Param("remainNum") int remainNum, @Param("id") String id);

    void updateProfit(@Param("profit") BigDecimal profit, @Param("id") String id);

    void updateBond(@Param("bond") BigDecimal bond, @Param("id") String id);

    MdTrade getSellOnePrice(@Param("groupId") String groupId, @Param("type") String type);
}
