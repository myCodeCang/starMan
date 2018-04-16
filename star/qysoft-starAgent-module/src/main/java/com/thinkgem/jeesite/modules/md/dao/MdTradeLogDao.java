package com.thinkgem.jeesite.modules.md.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.md.entity.MdTrade;
import com.thinkgem.jeesite.modules.md.entity.MdTradeLog;
import org.apache.ibatis.annotations.Param;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by kevin on 2017/10/2.
 */
@MyBatisDao
public interface MdTradeLogDao extends CrudDao<MdTradeLog> {

    List<MdTradeLog> findKunSunRanklist(MdTradeLog mdTradeLog);

    List<MdTradeLog> realTimeList(@Param("groupId") String groupId, @Param("date") Date date, @Param("limit") int limit);

    List<MdTradeLog> findEchartsList(@Param("groupId") String groupId, @Param("createDateBegin") String startDate, @Param("createDateEnd") String endDate);

    MdTradeLog findLastTradeLog(@Param("createDate") Date date, @Param("groupId") String groupId);

    MdTradeLog getProfitByDate(@Param("createDateBegin") String createDateBegin, @Param("createDateEnd") String createDateEnd);

    List<MdTradeLog> findChildVolume(MdTradeLog mdTradeLog);
}
