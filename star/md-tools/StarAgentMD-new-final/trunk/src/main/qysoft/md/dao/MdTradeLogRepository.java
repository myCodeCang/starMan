package main.qysoft.md.dao;

import main.qysoft.jpa.support.CustomRepository;
import main.qysoft.md.entity.MdTradeLog;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by kevin on 2017/10/18.
 */
public interface MdTradeLogRepository extends CustomRepository<MdTradeLog, Long>{
    MdTradeLog findByMdTradeId(long mdTradeId);

    List<MdTradeLog> findMdTradeLogsByGroupId(String groupId);

    Page<MdTradeLog> findByAuto(MdTradeLog mdTradeLog, Pageable pageable);

}
