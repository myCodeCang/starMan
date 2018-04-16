package main.qysoft.md.service;

import main.qysoft.md.dao.MdTradeMainRepository;
import main.qysoft.md.entity.MdTradeMain;
import main.qysoft.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by kevin on 2017/11/2.
 */
@Service
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception"})
public class MdTradeMainServivce {

    @Autowired
    MdTradeMainRepository mdTradeMainRepository;

    public Optional<MdTradeMain> findCurrentTransInfoByGroupId(String groupId) {
        if (StringUtils.isEmpty(groupId)) {
            return Optional.empty();
        }

        return Optional.ofNullable(mdTradeMainRepository.findCurrentTransInfoByGroupId(groupId));
    }

    public Optional<String> findIsOverTop(String groupId) {
        if (StringUtils.isEmpty(groupId)) {
            return Optional.empty();
        }

        return Optional.ofNullable(mdTradeMainRepository.findIsOverTop(groupId));
    }

    public List<MdTradeMain> findByGroupIdInAndCreateDate_Day(List<String> groupIds){
        List<MdTradeMain>  mdTradeMainList = null;
        if(groupIds.size()<1){
            return mdTradeMainList;
        }
        List<MdTradeMain> byGroupIdInAndCreateDateBetween = null;
        return  mdTradeMainRepository.findByGroupIdInAndCreateDateBetween(groupIds, DateUtil.getDiffDatetime(new Date(),-1) , new Date());
    }

}