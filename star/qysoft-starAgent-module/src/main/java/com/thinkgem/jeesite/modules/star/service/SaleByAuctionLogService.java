/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.star.dao.SaleByAuctionLogDao;
import com.thinkgem.jeesite.modules.star.entity.SaleByAuction;
import com.thinkgem.jeesite.modules.star.entity.SaleByAuctionLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 拍卖3Service
 * @author luo
 * @version 2017-09-25
 */
@Service
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class SaleByAuctionLogService extends CrudService<SaleByAuctionLogDao, SaleByAuctionLog> {

	public SaleByAuctionLog get(String id) {
		return super.get(id);
	}
	
	public List<SaleByAuctionLog> findList(SaleByAuctionLog saleByAuctionLog) {
		return super.findList(saleByAuctionLog);
	}
	
	public Page<SaleByAuctionLog> findPage(Page<SaleByAuctionLog> page, SaleByAuctionLog saleByAuctionLog) {
		return super.findPage(page, saleByAuctionLog);
	}
	

	public void save(SaleByAuctionLog saleByAuctionLog) {
		super.save(saleByAuctionLog);
	}
	

	public void delete(SaleByAuctionLog saleByAuctionLog) {
		super.delete(saleByAuctionLog);
	}


	public List<SaleByAuctionLog> getByAuctionIdAfterId(String auctionId, String idAfter) {
		return dao.getByAuctionIdAfterId(auctionId,idAfter);
	}

    public List<SaleByAuctionLog> findByUser(String auctionUser) {
		return dao.findByUser(auctionUser);
    }
}