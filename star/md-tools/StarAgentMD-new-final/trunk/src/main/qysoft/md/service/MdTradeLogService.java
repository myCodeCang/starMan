package main.qysoft.md.service;

import main.qysoft.md.dao.MdTradeLogRepository;
import main.qysoft.md.entity.MdTradeLog;
import main.qysoft.utils.DateUtil;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

/**
 * Created by kevin on 2017/11/4.
 */
@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackForClassName = {"RuntimeException", "Exception"})
public class MdTradeLogService {
    @Autowired
    MdTradeLogRepository mdTradeLogRepository;

    public void save(MdTradeLog tradeLog) {
        if (null == tradeLog) {
            return;
        }

        mdTradeLogRepository.save(tradeLog);
    }

    public List<MdTradeLog> findByGroupId(Integer groupId) {
        if (groupId == null) {
            return Lists.newArrayList();
        }

        return mdTradeLogRepository.findMdTradeLogsByGroupId(String.valueOf(groupId));
    }

    public Page<MdTradeLog> findByFilterCondition(String groupId, String userName, LocalDate createDateBegin,LocalDate createDateEnd,int pageNum) {
        MdTradeLog mdTradeLog = new MdTradeLog();
        mdTradeLog.setGroupId(groupId.toString());
        mdTradeLog.setUserName(userName);
        mdTradeLog.setAmount(null);
        mdTradeLog.setCreateDateBegin(DateUtil.formatLocaDateToDate(createDateBegin,0,0,0));
        mdTradeLog.setCreateDateEnd(DateUtil.formatLocaDateToDate(createDateEnd,23,59,59));

        return mdTradeLogRepository.findByAuto(mdTradeLog,new PageRequest(pageNum-1<0?0:pageNum-1,25));
    }
}
