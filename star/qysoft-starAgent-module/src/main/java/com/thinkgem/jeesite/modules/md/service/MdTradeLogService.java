package com.thinkgem.jeesite.modules.md.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.md.dao.MdTradeLogDao;
import com.thinkgem.jeesite.modules.md.entity.MdTradeLog;
import com.thinkgem.jeesite.modules.user.dao.UserUserinfoDao;
import com.thinkgem.jeesite.modules.user.entity.UserUserinfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by kevin on 2017/10/2.
 */
@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackForClassName = {"RuntimeException", "Exception", "ValidationException"})
public class MdTradeLogService extends CrudService<MdTradeLogDao, MdTradeLog> {
    @Resource
    private UserUserinfoDao userUserinfoDao;
    public MdTradeLog get(String id) {
        return super.get(id);
    }

    public List<MdTradeLog> findList(MdTradeLog mdTradeLog) {
        return super.findList(mdTradeLog);
    }

    public Page<MdTradeLog> findPage(Page<MdTradeLog> page, MdTradeLog mdTradeLog) {
        return super.findPage(page, mdTradeLog);
    }


    public void save(MdTradeLog mdTradeLog) {
        super.save(mdTradeLog);
    }


    public void delete(MdTradeLog mdTradeLog) {
        super.delete(mdTradeLog);
    }

    public Page<MdTradeLog> findKunSunRanklist(Page<MdTradeLog> mdTradeLogPage, MdTradeLog mdTradeLog) {
        mdTradeLog.setPage(mdTradeLogPage);
        mdTradeLogPage.setList(dao.findKunSunRanklist(mdTradeLog));
        return mdTradeLogPage;
    }

    public List<MdTradeLog> realTimeList(String groupId, Date date, int limit) {
        return dao.realTimeList(groupId,date,limit);
    }

    public List<MdTradeLog> findEchartsList(String groupId, String startDate, String endDate) {
        return dao.findEchartsList(groupId,startDate,endDate);
    }

    public MdTradeLog  findLastTradeLog(Date date, String groupId) {
        return dao.findLastTradeLog(date,groupId);
    }

    public MdTradeLog getProfitByDate(String createDateBegin,String createDateEnd) {
        return dao.getProfitByDate(createDateBegin,createDateEnd);
    }

    public Page<UserUserinfo>  findAgentVolume(Page<UserUserinfo> userUserinfoPage, MdTradeLog mdTradeLog,  UserUserinfo userinfo) {
        userinfo.setUserTypeBegin("1");
        List<UserUserinfo> userinfoList = userUserinfoDao.findList(userinfo);
        mdTradeLog.setParentListLike(userinfo.getId());
        List<MdTradeLog> tradeLogList = dao.findChildVolume(mdTradeLog);
        for(UserUserinfo userUserinfo :userinfoList){
            BigDecimal volume = tradeLogList.stream().filter(p -> p.getUserName().equals(userUserinfo.getUserName()) || p.getParentListLike().indexOf(userUserinfo.getId()) > -1).map(MdTradeLog::getVolume).reduce(BigDecimal.ZERO, BigDecimal::add);
            userUserinfo.setMoney6(volume);
        }
        userinfo.setPage(userUserinfoPage);
        userUserinfoPage.setList(userinfoList);
        return userUserinfoPage;
    }
}
