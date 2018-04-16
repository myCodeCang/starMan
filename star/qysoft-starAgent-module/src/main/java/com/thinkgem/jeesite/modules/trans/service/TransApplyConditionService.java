/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.service;

import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.trans.dao.TransApplyConditionDao;
import com.thinkgem.jeesite.modules.trans.entity.TransApplyCondition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订货限制Service
 *
 * @author xueyuliang
 * @version 2017-05-09
 */
@Service
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class TransApplyConditionService extends CrudService<TransApplyConditionDao, TransApplyCondition> {

    public TransApplyCondition get(String id) {
        return super.get(id);
    }

    public List<TransApplyCondition> findList(TransApplyCondition transApplyCondition) {
        return super.findList(transApplyCondition);
    }

    public Page<TransApplyCondition> findPage(Page<TransApplyCondition> page, TransApplyCondition transApplyCondition) {
        return super.findPage(page, transApplyCondition);
    }


    public void save(TransApplyCondition transApplyCondition) throws ValidationException {
        super.save(transApplyCondition);
    }


    public void delete(TransApplyCondition transApplyCondition)throws ValidationException {
        super.delete(transApplyCondition);
    }


    public void deleteByApplyid(String id) {
        dao.deleteByApplyid(id);
    }

    public List<TransApplyCondition> getByApplyId(String id) {
       return  dao.getByApplyId(id);
    }
}