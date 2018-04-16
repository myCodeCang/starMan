/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.service;

import com.alibaba.druid.support.json.JSONUtils;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.md.dao.MdTradeLogDao;
import com.thinkgem.jeesite.modules.md.entity.MdTradeLog;
import com.thinkgem.jeesite.modules.star.dao.BaseGoodsGroupDao;
import com.thinkgem.jeesite.modules.star.entity.BaseGoodsGroup;
import com.thinkgem.jeesite.modules.trans.service.TransBuyDayTrendService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基础商品Service
 * @author luojie
 * @version 2017-09-25
 */
@Service
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class BaseGoodsGroupService extends CrudService<BaseGoodsGroupDao, BaseGoodsGroup> {

	@Autowired
	private TransBuyDayTrendService transBuyDayTrendService;
	@Autowired
	private MdTradeLogDao mdTradeLogDao;

	public BaseGoodsGroup get(String id) {
		return super.get(id);
	}
	
	public List<BaseGoodsGroup> findList(BaseGoodsGroup baseGoodsGroup) {
		return super.findList(baseGoodsGroup);
	}
	
	public Page<BaseGoodsGroup> findPage(Page<BaseGoodsGroup> page, BaseGoodsGroup baseGoodsGroup) {
		return super.findPage(page, baseGoodsGroup);
	}
	

	public void save(BaseGoodsGroup baseGoodsGroup) {
		super.save(baseGoodsGroup);
	}
	

	public void delete(BaseGoodsGroup baseGoodsGroup) {
		super.delete(baseGoodsGroup);
	}


	public void updateStatusTime(String id, String status, Date transStartTime) {
		dao.updateStatusTime(id,status,transStartTime);
	}

    public List<BaseGoodsGroup> findTradeList(BaseGoodsGroup baseGoodsGroup) {
		List<BaseGoodsGroup> baseGoodsList = dao.findTradeList(baseGoodsGroup);
		MdTradeLog mdTradeLog = new MdTradeLog();
		mdTradeLog.setCreateDate(new Date());
		List<MdTradeLog> list = mdTradeLogDao.findList(mdTradeLog);
		for (BaseGoodsGroup baseGoods : baseGoodsList){
			int sum = list.stream().filter(p -> p.getGroupId().equals(baseGoods.getId())).mapToInt(MdTradeLog::getAmount).sum();
			baseGoods.setSum(sum);
			BigDecimal nowPrice = getNowPrice(baseGoods);
			baseGoods.getMdTradeMain().setTransactionPrice(nowPrice);
		}
		return baseGoodsList ;
    }

	private BigDecimal getNowPrice(BaseGoodsGroup baseGoods) {
		File path = new File(this.getClass().getResource("/").getPath());
		StringBuffer pa = new StringBuffer();
		pa.append(path).append(File.separator).append("nowprice").append(File.separator).append(baseGoods.getId()).append(".txt");
		BigDecimal nowPrice = BigDecimal.ZERO;
		Map<String,String> map= new HashMap<>();
		try {
			String strJson = FileUtils.readFileToString(new File(pa.toString()));
			map = (Map<String, String>) JSONUtils.parse(strJson);
			nowPrice = new BigDecimal(map.get("price"));
		} catch (Exception e){
		}
		return nowPrice;
	}

    public void updateBaseMoney(String id, BigDecimal money) {
		dao.updateBaseMoney(id,money);
    }

    public void updateBaseNum(String groupId, int num) {
		dao.updateBaseNum(groupId,num);
    }
}