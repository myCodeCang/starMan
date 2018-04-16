/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.service;

import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.modules.trans.dao.TransBuyDayTrendDao;
import com.thinkgem.jeesite.modules.trans.entity.TransBuyDayTrend;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易历史价格表Service
 *
 * @author luo
 * @version 2017-05-10
 */
@Service
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class TransBuyDayTrendService extends CrudService<TransBuyDayTrendDao, TransBuyDayTrend> {

    public TransBuyDayTrend get(String id) {
        return super.get (id);
    }

    public List<TransBuyDayTrend> findList(TransBuyDayTrend transBuyDayTrend) {
        return super.findList (transBuyDayTrend);
    }

    public Page<TransBuyDayTrend> findPage(Page<TransBuyDayTrend> page, TransBuyDayTrend transBuyDayTrend) {
        return super.findPage (page, transBuyDayTrend);
    }


    public void save(TransBuyDayTrend transBuyDayTrend) throws ValidationException {
        super.save (transBuyDayTrend);
    }


    public void delete(TransBuyDayTrend transBuyDayTrend) throws ValidationException {
        super.delete (transBuyDayTrend);
    }

    public List<TransBuyDayTrend> getChartDatasBuyGroup(String groupId) {
        if (StringUtils2.isBlank(groupId)) {
            return new ArrayList<>();
        }

        return dao.getChartDatasBuyGroup(groupId);
    }
}