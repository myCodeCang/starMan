/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.service;

import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.star.dao.ShopClassifyDao;
import com.thinkgem.jeesite.modules.star.entity.BaseGoodsGroup;
import com.thinkgem.jeesite.modules.star.entity.ShopClassify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商家分类Service
 * @author luojie
 * @version 2017-07-14
 */
@Service
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class ShopClassifyService extends CrudService<ShopClassifyDao, ShopClassify> {
	@Autowired
	private BaseGoodsGroupService baseGoodsGroupService;

	public ShopClassify get(String id) {
		return super.get(id);
	}
	
	public List<ShopClassify> findList(ShopClassify shopClassify) {
		return super.findList(shopClassify);
	}
	
	public Page<ShopClassify> findPage(Page<ShopClassify> page, ShopClassify shopClassify) {
		return super.findPage(page, shopClassify);
	}
	

	public void save(ShopClassify shopClassify) {
		super.save(shopClassify);
	}
	

	public void delete(ShopClassify shopClassify) throws ValidationException{
		BaseGoodsGroup baseGoodsGroup = new BaseGoodsGroup();
		baseGoodsGroup.setCategoryId(shopClassify.getId());
		List<BaseGoodsGroup> list = baseGoodsGroupService.findList(baseGoodsGroup);
		if(list.size()>0){
			throw new ValidationException("该分类下存在明星,不可删除!");
		}
		super.delete(shopClassify);
	}
	
}