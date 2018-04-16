/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.service;

import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.trans.dao.TransGoodsGroupDao;
import com.thinkgem.jeesite.modules.trans.entity.TransApply;
import com.thinkgem.jeesite.modules.trans.entity.TransBuyDayTrend;
import com.thinkgem.jeesite.modules.trans.entity.TransGoods;
import com.thinkgem.jeesite.modules.trans.entity.TransGoodsGroup;
import com.thinkgem.jeesite.modules.user.entity.UserSequence;
import com.thinkgem.jeesite.modules.user.service.UserSequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 交易品表Service
 *
 * @author luojie
 * @version 2017-05-08
 */
@Service
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})

public class TransGoodsGroupService extends CrudService<TransGoodsGroupDao, TransGoodsGroup> {

	@Autowired
	private TransBuyDayTrendService transBuyDayTrendService;
	@Autowired
	private TransGoodsService transGoodsService ;

	@Autowired
	private TransApplyService transApplyService ;

    @Autowired
    private UserSequenceService userSequenceService ;


    public TransGoodsGroup get(String id) {
        return super.get(id);
    }


    public List<TransGoodsGroup> findList(TransGoodsGroup transGoodsGroup) {
        return super.findList(transGoodsGroup);
    }


    public Page<TransGoodsGroup> findPage(Page<TransGoodsGroup> page, TransGoodsGroup transGoodsGroup) {
        return super.findPage(page, transGoodsGroup);
    }


    public void save(TransGoodsGroup transGoodsGroup) throws ValidationException {

        if (transGoodsGroup.getIsNewRecord()){
            //如果是新加入的商品.在系统序列中添加与之对应的序列.
            transGoodsGroup.preInsert();
            UserSequence userSequence = new UserSequence();
            userSequence.setName("trans_group_"+transGoodsGroup.getId());
            userSequence.setCurrentValue("10000000");
            userSequence.setIncrement("1");
            userSequence.setRemarks(transGoodsGroup.getName());
            userSequenceService.save(userSequence);

            dao.insert(transGoodsGroup);
        }else{
            transGoodsGroup.preUpdate();
            dao.update(transGoodsGroup);
        }

    }



    public void delete(TransGoodsGroup transGoodsGroup) throws ValidationException {

	    TransGoods transGoods = new TransGoods();
	    transGoods.setGroupId(transGoodsGroup.getId());
        List<TransGoods> transGoodsList =  transGoodsService.findListLock(transGoods);
        if(transGoodsList.size() > 0){
            throw new ValidationException("该艺术品已经有用户持有,不可删除!");
        }

        TransApply transApply = new TransApply();
        transApply.setGroupId(transGoodsGroup.getId());
        List<TransApply> transApplies =  transApplyService.findList(transApply);
        if(transApplies.size() >0){
            throw new ValidationException("该艺术品发布了订货委托, 请先删除订货委托,之后再进行操作!");
        }

        super.delete(transGoodsGroup);


    }
	public List<TransGoodsGroup> getTransListWithTrend(Date trendDate, String status) {

		List<TransGoodsGroup> transGoodsGroups = dao.getTransListWithTrend(trendDate,status);

		return transGoodsGroups;
	}
	public TransGoodsGroup getWithTrend(String id, Date seachDate) {
		TransGoodsGroup transGoodsGroup = super.get(id);

        TransBuyDayTrend dayTrend = new TransBuyDayTrend();
        dayTrend.setGroupId(id);
        dayTrend.setAddDate(seachDate);

        List<TransBuyDayTrend> transBuyDayTrendList =  transBuyDayTrendService.findList(dayTrend);
		transGoodsGroup.setTransBuyDayTrendList(transBuyDayTrendList);

		return transGoodsGroup;
	}

}