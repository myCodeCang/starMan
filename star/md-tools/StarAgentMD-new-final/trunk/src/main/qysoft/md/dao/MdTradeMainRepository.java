package main.qysoft.md.dao;

import main.qysoft.jpa.support.CustomRepository;
import main.qysoft.md.entity.MdTradeMain;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by kevin on 2017/10/18.
 */
public interface MdTradeMainRepository extends CustomRepository<MdTradeMain, Long> {
    @Query(value = "select * from md_trade_main where group_id = ?1 order by create_date desc limit 1", nativeQuery = true)
    MdTradeMain findCurrentTransInfoByGroupId(String groupId);

    @Query(value = "select * from md_trade_main where group_id = ?1 order by create_date asc limit 1", nativeQuery = true)
    MdTradeMain findFirstTransInfoByGroupId(String groupId);

    @Query(value = "select * from md_trade_main where create_date >= DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 1 day), '%Y-%m-%d 00:00:00') and create_date <= DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 1 day), '%Y-%m-%d 23:59:59')", nativeQuery = true)
    List<MdTradeMain> findLastDayTrans();

    @Query(value = "select * from md_trade_main where create_date >= DATE_FORMAT(NOW(), '%Y-%m-%d 00:00:00') and create_date <= DATE_FORMAT(NOW(), '%Y-%m-%d 23:59:59')", nativeQuery = true)
    List<MdTradeMain> findCurrentDayTrans();

    @Query(value = "select is_over_top from md_trade_main where group_id = ?1 order by create_date desc limit 1", nativeQuery = true)
    String findIsOverTop(String groupId);

    List<MdTradeMain> findByGroupIdInAndCreateDateBetween(Collection<String> groupIds, Date date, Date date2);

}
