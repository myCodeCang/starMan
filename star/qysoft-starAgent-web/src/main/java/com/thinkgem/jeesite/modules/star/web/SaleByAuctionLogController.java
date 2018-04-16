/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.star.entity.SaleByAuctionLog;
import com.thinkgem.jeesite.modules.star.service.SaleByAuctionLogService;
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
 * 拍卖3Controller
 * @author luo
 * @version 2017-09-25
 */
@Controller
@RequestMapping(value = "${adminPath}/star/saleByAuctionLog")
public class SaleByAuctionLogController extends BaseController {

	@Autowired
	private SaleByAuctionLogService saleByAuctionLogService;
	
	@ModelAttribute
	public SaleByAuctionLog get(@RequestParam(required=false) String id) {
		SaleByAuctionLog entity = null;
		if (StringUtils2.isNotBlank(id)){
			entity = saleByAuctionLogService.get(id);
		}
		if (entity == null){
			entity = new SaleByAuctionLog();
		}
		return entity;
	}
	
	@RequiresPermissions("user:saleByAuctionLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(SaleByAuctionLog saleByAuctionLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SaleByAuctionLog> page = saleByAuctionLogService.findPage(new Page<SaleByAuctionLog>(request, response), saleByAuctionLog); 
		model.addAttribute("page", page);
		return "modules/star/saleByAuctionLogList";
	}

	@RequiresPermissions("user:saleByAuctionLog:view")
	@RequestMapping(value = "form")
	public String form(SaleByAuctionLog saleByAuctionLog, Model model) {
		model.addAttribute("saleByAuctionLog", saleByAuctionLog);
		return "modules/star/saleByAuctionLogForm";
	}

	@RequiresPermissions("user:saleByAuctionLog:edit")
	@RequestMapping(value = "save")
	public String save(SaleByAuctionLog saleByAuctionLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, saleByAuctionLog)){
			return form(saleByAuctionLog, model);
		}
		saleByAuctionLogService.save(saleByAuctionLog);
		addMessage(redirectAttributes, "保存拍卖纪录成功");
		return "redirect:"+Global.getAdminPath()+"/star/saleByAuctionLog/?repage";
	}
	
	@RequiresPermissions("user:saleByAuctionLog:edit")
	@RequestMapping(value = "delete")
	public String delete(SaleByAuctionLog saleByAuctionLog, RedirectAttributes redirectAttributes) {
		saleByAuctionLogService.delete(saleByAuctionLog);
		addMessage(redirectAttributes, "删除拍卖纪录列表成功");
		return "redirect:"+Global.getAdminPath()+"/star/saleByAuctionLog/?repage";
	}

}