/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.star.entity.AuctionGuarantee;
import com.thinkgem.jeesite.modules.star.service.AuctionGuaranteeService;
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
 * 拍卖Controller
 * @author luo
 * @version 2017-09-25
 */
@Controller
@RequestMapping(value = "${adminPath}/star/auctionGuarantee")
public class AuctionGuaranteeController extends BaseController {

	@Autowired
	private AuctionGuaranteeService auctionGuaranteeService;
	
	@ModelAttribute
	public AuctionGuarantee get(@RequestParam(required=false) String id) {
		AuctionGuarantee entity = null;
		if (StringUtils2.isNotBlank(id)){
			entity = auctionGuaranteeService.get(id);
		}
		if (entity == null){
			entity = new AuctionGuarantee();
		}
		return entity;
	}
	
	@RequiresPermissions("user:auctionGuarantee:view")
	@RequestMapping(value = {"list", ""})
	public String list(AuctionGuarantee auctionGuarantee, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<AuctionGuarantee> page = auctionGuaranteeService.findPage(new Page<AuctionGuarantee>(request, response), auctionGuarantee); 
		model.addAttribute("page", page);
		return "modules/star/auctionGuaranteeList";
	}

	@RequiresPermissions("user:auctionGuarantee:view")
	@RequestMapping(value = "form")
	public String form(AuctionGuarantee auctionGuarantee, Model model) {
		model.addAttribute("auctionGuarantee", auctionGuarantee);
		return "modules/star/auctionGuaranteeForm";
	}

	@RequiresPermissions("user:auctionGuarantee:edit")
	@RequestMapping(value = "save")
	public String save(AuctionGuarantee auctionGuarantee, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, auctionGuarantee)){
			return form(auctionGuarantee, model);
		}
		auctionGuaranteeService.save(auctionGuarantee);
		addMessage(redirectAttributes, "保存拍卖成功");
		return "redirect:"+Global.getAdminPath()+"/star/auctionGuarantee/?repage";
	}
	
	@RequiresPermissions("user:auctionGuarantee:edit")
	@RequestMapping(value = "delete")
	public String delete(AuctionGuarantee auctionGuarantee, RedirectAttributes redirectAttributes) {
		auctionGuaranteeService.delete(auctionGuarantee);
		addMessage(redirectAttributes, "删除拍卖成功");
		return "redirect:"+Global.getAdminPath()+"/star/auctionGuarantee/?repage";
	}

}