/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.service;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.trans.dao.TransGoodsGroupDao;
import com.thinkgem.jeesite.modules.trans.dao.TransOrderDao;
import com.thinkgem.jeesite.modules.trans.entity.TransGoods;
import com.thinkgem.jeesite.modules.trans.entity.TransGoodsGroup;
import com.thinkgem.jeesite.modules.trans.entity.TransOrder;
import com.thinkgem.jeesite.modules.user.entity.UserAddress;
import com.thinkgem.jeesite.modules.user.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 提货订单Service
 * @author ss
 * @version 2017-05-08
 */
@Service
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class TransOrderService extends CrudService<TransOrderDao, TransOrder> {

	@Autowired
	private TransGoodsService transGoodsDao;

	@Autowired
	private TransGoodsGroupDao transGoodsGroupDao;

	@Autowired
	private UserAddressService userAddressDao;


	public TransOrder get(String id) {
		return super.get(id);
	}
	
	public List<TransOrder> findList(TransOrder transOrder) {
		return super.findList(transOrder);
	}
	
	public Page<TransOrder> findPage(Page<TransOrder> page, TransOrder transOrder) {
		return super.findPage(page, transOrder);
	}

	/**
	 * 锁表查询
	 * @param transOrder
	 * @return
	 */
	@Transactional(isolation = Isolation.REPEATABLE_READ,readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
	public List<TransOrder> findListLock(TransOrder transOrder) {
		return dao.findListLock(transOrder);
	}



    public void updateType(TransOrder transOrder) throws ValidationException {
		TransOrder currOrder = get(transOrder.getId());
		if(!currOrder.getType().equals(EnumUtil.CheckType.uncheck.toString())){
			throw  new ValidationException("该订单已处理,不可重复操作!");
		}

		dao.updateType(transOrder);
    }

	/**
	 * 申请提货

	 */

	public void applyPickGoods(String userName, String groupId , int num,String addressId,String remarks) throws ValidationException {


		TransGoodsGroup transGoodsGroup =   transGoodsGroupDao.get(groupId);
		if(transGoodsGroup == null){
			throw new ValidationException("提货商品不存在,请重新尝试");
		}

		UserAddress userAddress =  userAddressDao.get(addressId);
		if(userAddress == null || !userAddress.getUserName().equals(userName)){
			throw new ValidationException("收货地址有误!");
		}

		//判断用户交易品充足
		TransGoods transGoods = new TransGoods();
		transGoods.setGroupId(groupId);
		transGoods.setUserName(userName);
		transGoods.setStatus(EnumUtil.GoodsType.hold.toString());

		List<TransGoods> transGoodsList = transGoodsDao.findListLock(transGoods);
		if (transGoodsList.size() < num) {
			throw new ValidationException("当前持有量不足");
		}


		for(int i=0;i<num;i++){
			TransGoods goodsTemp =  transGoodsList.get(i);
			goodsTemp.setStatus(EnumUtil.GoodsType.ordre.toString());
			goodsTemp.setSellMoney(goodsTemp.getBuyMoney());
			transGoodsDao.save (goodsTemp);
		}

		TransOrder transOrder = new TransOrder ();
		transOrder.setUserName (userAddress.getUserName ());
		transOrder.setGroupId (transGoodsGroup.getId());
		transOrder.setGoodsName (transGoodsGroup.getName());
		transOrder.setMobile (userAddress.getMobile ());
		transOrder.setAddress (userAddress.getAddress ());
		transOrder.setRemarks(remarks);
		transOrder.setNum(num);
		transOrder.setConsignee (userAddress.getTrueName ());
		transOrder.setPostCode (userAddress.getPostcode ());
		transOrder.setType (EnumUtil.CheckType.uncheck.toString());
		this.save (transOrder);
	}

}