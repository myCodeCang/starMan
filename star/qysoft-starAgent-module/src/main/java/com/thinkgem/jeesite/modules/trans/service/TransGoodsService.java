/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.service;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.star.service.BaseGoodsGroupService;
import com.thinkgem.jeesite.modules.trans.dao.TransGoodsDao;
import com.thinkgem.jeesite.modules.trans.entity.TransGoods;
import com.thinkgem.jeesite.modules.trans.entity.TransGoodsKuiSun;
import com.thinkgem.jeesite.modules.trans.entity.TransGoodsNumReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 交易品持仓Service
 * @author luo
 * @version 2017-05-10
 */
@Service
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class TransGoodsService extends CrudService<TransGoodsDao, TransGoods> {

	@Autowired
	private BaseGoodsGroupService baseGoodsGroupService ;
	public TransGoods get(String id) {
		return super.get(id);
	}
	
	public List<TransGoods> findList(TransGoods transGoods) {
		return super.findList(transGoods);
	}
	
	public Page<TransGoods> findPage(Page<TransGoods> page, TransGoods transGoods) {
		return super.findPage(page, transGoods);
	}

	public TransGoods getUserStar(String groupId,String userName){
		return dao.getUserStar(groupId,userName);
	}

	public void save(TransGoods transGoods) throws ValidationException {
		super.save(transGoods);
	}
	

	public void delete(TransGoods transGoods) throws ValidationException {
		super.delete(transGoods);
	}

	@Transactional(isolation = Isolation.REPEATABLE_READ,readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
	public List<TransGoods> findListLock(TransGoods transGoods) {
		return dao.findListLock(transGoods);
	}


	/**
	 * 分组统计用户交易品情况
	 * @param
	 * @param entity
	 * @return
	 */
	public List<TransGoods> findByGroup(TransGoods entity) {
		return dao.findByGroup(entity);
	}


	/**
	 * 查询持有交易品数量
	 * @param groupId
	 * @param userName
	 * @param status
	 * @return
	 */
	public int getHoldGoodsNum(String groupId, String userName, String status) {

		TransGoods transGoods = new TransGoods();
		transGoods.setGroupId(groupId);
		transGoods.setUserName(userName);
		transGoods.setStatus(status);
		List<TransGoods> transGoodsList =  dao.findList(transGoods);
		return   transGoodsList.size();
	}


	/**
	 * 查询一个用户一段时间内的亏损积分
	 * @param userName
	 * @param beginSelltime
	 * @param endSelltime
	 * @return
	 */
	public TransGoodsKuiSun getKuiSunMoneyByName(String userName , Date beginSelltime , Date endSelltime){
		TransGoodsKuiSun transGoods = new TransGoodsKuiSun();
		transGoods.setUserName(userName);
		transGoods.setBeginSelltime(beginSelltime);
		transGoods.setEndSelltime(endSelltime);
		List<TransGoodsKuiSun> transGoodsKuiSuns =  dao.findKunSunRanklist(transGoods);
		if(transGoodsKuiSuns.size() >0){
			return  transGoodsKuiSuns.get(0);
		}
		return null;
	}

	/**
	 * 统计所有用户亏损排行
	 * @param transGoodsPage
	 * @param transGoods
	 * @return
	 */
	public Page<TransGoodsKuiSun> findKunSunRanklist(Page<TransGoodsKuiSun> transGoodsPage, TransGoodsKuiSun transGoods) {
		transGoods.setPage (transGoodsPage);
		transGoodsPage.setList(dao.findKunSunRanklist(transGoods));
		return transGoodsPage;
	}

	/**
	 * 修改用户持有的明星时间
	 * @param userName
	 * @param groupId
	 * @param changeNum
	 */
	public void updateGoodsNum(String userName, String groupId, int changeNum) {

		TransGoods transGoodsBuy = this.getUserStar(groupId,userName);
		if(transGoodsBuy == null) {
			transGoodsBuy = new TransGoods();
			transGoodsBuy.setUserName(userName);
			transGoodsBuy.setStatus(EnumUtil.GoodsType.hold.toString());
			transGoodsBuy.setGroupId(groupId);
		}
		transGoodsBuy.setNum(transGoodsBuy.getNum()+changeNum);
		this.save(transGoodsBuy);
	}


    public TransGoodsNumReport getTransGoodsNumReport(String groupId) {
		TransGoodsNumReport transGoodsNumReport = dao.getGoodsNumReport(groupId);
		if(transGoodsNumReport == null){
			transGoodsNumReport = new TransGoodsNumReport();
		}
		return  transGoodsNumReport;
    }
	public TransGoods getByBuyId(String buyId){
		return dao.getByBuyId(buyId);
	}

	public void updateNum(String id, int num) {
		dao.updateNum(id,num);
	}

	public TransGoods getLock(String id) {
		return dao.getLock(id);
	}

    public void deleteExceed(String[] groupIdArray) {
		dao.deleteExceed(groupIdArray);
    }
}