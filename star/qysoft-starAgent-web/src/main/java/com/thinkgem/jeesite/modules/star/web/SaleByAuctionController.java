/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.star.entity.SaleByAuction;
import com.thinkgem.jeesite.modules.star.service.SaleByAuctionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拍卖2Controller
 * @author luo
 * @version 2017-09-25
 */
@Controller
@RequestMapping(value = "${adminPath}/star/saleByAuction")
public class SaleByAuctionController extends BaseController {

	@Autowired
	private SaleByAuctionService saleByAuctionService;
	
	@ModelAttribute
	public SaleByAuction get(@RequestParam(required=false) String id) {
		SaleByAuction entity = null;
		if (StringUtils2.isNotBlank(id)){
			entity = saleByAuctionService.get(id);
		}
		if (entity == null){
			entity = new SaleByAuction();
		}
		return entity;
	}
	
	@RequiresPermissions("user:saleByAuction:view")
	@RequestMapping(value = {"list", ""})
	public String list(SaleByAuction saleByAuction, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SaleByAuction> page = saleByAuctionService.findPage(new Page<SaleByAuction>(request, response), saleByAuction); 
		model.addAttribute("page", page);
		return "modules/star/saleByAuctionList";
	}

	@RequiresPermissions("user:saleByAuction:view")
	@RequestMapping(value = "form")
	public String form(SaleByAuction saleByAuction, Model model) {
		model.addAttribute("saleByAuction", saleByAuction);
		return "modules/star/saleByAuctionForm";
	}

	@RequiresPermissions("user:saleByAuction:edit")
	@RequestMapping(value = "save")
	public String save(SaleByAuction saleByAuction, Model model, RedirectAttributes redirectAttributes) {

		try {
			saleByAuctionService.save(saleByAuction);
		} catch (ValidationException e) {
			addMessage(model, "失败:"+e.getMessage());
			return form(saleByAuction, model);
		}
		addMessage(redirectAttributes, "保存拍卖产品成功");
		return "redirect:"+Global.getAdminPath()+"/star/saleByAuction/?repage";
	}
	
	@RequiresPermissions("user:saleByAuction:edit")
	@RequestMapping(value = "delete")
	public String delete(SaleByAuction saleByAuction, RedirectAttributes redirectAttributes) {
		saleByAuctionService.delete(saleByAuction);
		addMessage(redirectAttributes, "删除拍卖产品成功");
		return "redirect:"+Global.getAdminPath()+"/star/saleByAuction/?repage";
	}


	@RequestMapping(value = "auctionSuccess")
	public String auctionSuccess(SaleByAuction saleByAuction, Model model, RedirectAttributes redirectAttributes) {
		try {
			saleByAuctionService.auctionSuccess(saleByAuction.getId());
		} catch (ValidationException e) {
			addMessage(model, "失败:"+e.getMessage());
			return form(saleByAuction, model);
		}
		return "redirect:"+Global.getAdminPath()+"/star/saleByAuction/?repage";
	}

}