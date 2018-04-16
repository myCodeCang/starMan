package com.thinkgem.jeesite.modules.md.service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.md.dao.MdTradeLogDao;
import com.thinkgem.jeesite.modules.md.dao.MdTradeMainDao;
import com.thinkgem.jeesite.modules.md.entity.MdTradeLog;
import com.thinkgem.jeesite.modules.md.entity.MdTradeMain;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by kevin on 2017/10/2.
 */
@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackForClassName = {"RuntimeException", "Exception", "ValidationException"})
public class MdTradeMainService extends CrudService<MdTradeMainDao, MdTradeMain> {
    public MdTradeMain getNowMoney(String groupId) {
        return dao.getNowMoney(groupId);
    }

    public List<MdTradeMain> sumTransDayGroupByGroupId() {
        return dao.sumTransDayGroupByGroupId();
    }

    public MdTradeMain getPublishByGroupId(String groupId) {
        return dao.getPublishByGroupId(groupId);
    }

    public void updateIsOverTop(String id, String isOverTop){
        dao.updateIsOverTop(id,isOverTop);
    }

    public void updateClosingPrice(BigDecimal closePrice, Date createDate, String groupId) {
        dao.updateClosingPrice(closePrice,createDate,groupId);
    }
}
