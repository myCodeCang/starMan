/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.star.entity.ShopClassify;
import com.thinkgem.jeesite.modules.star.service.ShopClassifyService;
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
 * 商家分类Controller
 * @author luojie
 * @version 2017-07-14
 */
@Controller
@RequestMapping(value = "${adminPath}/star/shopClassify")
public class ShopClassifyController extends BaseController {

	@Autowired
	private ShopClassifyService shopClassifyService;

	@ModelAttribute
	public ShopClassify get(@RequestParam(required=false) String id) {
		ShopClassify entity = null;
		if (StringUtils2.isNotBlank(id)){
			entity = shopClassifyService.get(id);
		}
		if (entity == null){
			entity = new ShopClassify();
		}
		return entity;
	}

	@RequiresPermissions("user:shopClassify:view")
	@RequestMapping(value = {"list", ""})
	public String list(ShopClassify shopClassify, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ShopClassify> page = shopClassifyService.findPage(new Page<ShopClassify>(request, response), shopClassify);
		model.addAttribute("page", page);
		return "modules/star/shopClassifyList";
	}

	@RequiresPermissions("user:shopClassify:view")
	@RequestMapping(value = "form")
	public String form(ShopClassify shopClassify, Model model) {
		model.addAttribute("shopClassify", shopClassify);
		return "modules/star/shopClassifyForm";
	}

	@RequiresPermissions("user:shopClassify:edit")
	@RequestMapping(value = "save")
	public String save(ShopClassify shopClassify, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, shopClassify)){
			return form(shopClassify, model);
		}
		shopClassifyService.save(shopClassify);
		addMessage(redirectAttributes, "保存分类成功");
		return "redirect:"+ Global.getAdminPath()+"/star/shopClassify/?repage";
	}

	@RequiresPermissions("user:shopClassify:edit")
	@RequestMapping(value = "delete")
	public String delete(ShopClassify shopClassify, RedirectAttributes redirectAttributes) {
		try {
			shopClassifyService.delete(shopClassify);
		} catch (ValidationException e) {
			addMessage(redirectAttributes, "失败: "+e.getMessage());
			return "redirect:"+ Global.getAdminPath()+"/star/shopClassify/?repage";
		}
		addMessage(redirectAttributes, "删除分类成功");
		return "redirect:"+ Global.getAdminPath()+"/star/shopClassify/?repage";
	}

}