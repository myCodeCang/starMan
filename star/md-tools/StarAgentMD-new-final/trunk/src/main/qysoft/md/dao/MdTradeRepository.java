package main.qysoft.md.dao;

import main.qysoft.jpa.support.CustomRepository;
import main.qysoft.md.entity.MdTrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * Created by kevin on 2017/10/18.
 */
public interface MdTradeRepository extends JpaRepository<MdTrade, Long> {
    //@Query("select m from MdTrade m where (m.id > ?1 and m.state = 4) or (m.state = 4 and m.lastState <> 0)")
    @Query(value = "select * from md_trade m where (m.id > ?1 and m.state = 4) or ((m.state = 4 or m.state = 1) and m.last_state <> 3)", nativeQuery = true)
    List<MdTrade> getNewPublishedTrans(long lastQueryId);

    @Query(value = "select * from md_trade m where ((m.id > ?2 and m.state = 4) or ((m.state = 4 or m.state = 1) and m.last_state <> 3)) and m.group_id = ?1", nativeQuery = true)
    List<MdTrade> getNewPublishedTrans(String grouupId, long lastQueryId);

    @Query(value = "select DISTINCT m.group_id from md_trade m where (m.id > ?1 and m.state = 4) or (m.state = 4 and m.last_state <> 3)", nativeQuery = true)
    List<String> findGroupIds();

    //@Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select * from md_trade where id = ?1 limit 1 for update", nativeQuery = true)
    MdTrade findLockById(long tradeId);

    //@Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select * from md_trade where id = ?1 or id = ?2 for update", nativeQuery = true)
    List<MdTrade> findLockByIds(long id1, long id2);

    @Query(value = "select DISTINCT group_id from md_trade m", nativeQuery = true)
    List<Integer> findPublishedGroupIds();
}
