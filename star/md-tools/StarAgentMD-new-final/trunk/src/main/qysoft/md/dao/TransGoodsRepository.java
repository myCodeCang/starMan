package main.qysoft.md.dao;

import main.qysoft.jpa.support.CustomRepository;
import main.qysoft.md.entity.TransGoods;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * Created by kevin on 2017/11/4.
 */
public interface TransGoodsRepository extends CustomRepository<TransGoods, Long> {
    //@Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select * from trans_goods t where t.user_name = ?1 and t.group_id = ?2 limit 1 for update", nativeQuery = true)
    TransGoods findLockByUserNameAndGroupId(String userName, String groupId);

    @Query(value = "select DISTINCT group_id from trans_goods t", nativeQuery = true)
    List<Long> findPublishedGoodsId();
}
