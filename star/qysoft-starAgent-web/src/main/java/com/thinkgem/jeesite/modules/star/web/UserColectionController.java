/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.star.entity.UserColection;
import com.thinkgem.jeesite.modules.star.service.UserColectionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils2;

/**
 * 收藏表Controller
 * @author luojie
 * @version 2017-09-28
 */
@Controller
@RequestMapping(value = "${adminPath}/star/userColection")
public class UserColectionController extends BaseController {

	@Autowired
	private UserColectionService userColectionService;
	
	@ModelAttribute
	public UserColection get(@RequestParam(required=false) String id) {
		UserColection entity = null;
		if (StringUtils2.isNotBlank(id)){
			entity = userColectionService.get(id);
		}
		if (entity == null){
			entity = new UserColection();
		}
		return entity;
	}
	
	@RequiresPermissions("star:userColection:view")
	@RequestMapping(value = {"list", ""})
	public String list(UserColection userColection, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UserColection> page = userColectionService.findPage(new Page<UserColection>(request, response), userColection); 
		model.addAttribute("page", page);
		return "modules/star/userColectionList";
	}

	@RequiresPermissions("star:userColection:view")
	@RequestMapping(value = "form")
	public String form(UserColection userColection, Model model) {
		model.addAttribute("userColection", userColection);
		return "modules/star/userColectionForm";
	}

	@RequiresPermissions("star:userColection:edit")
	@RequestMapping(value = "save")
	public String save(UserColection userColection, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, userColection)){
			return form(userColection, model);
		}
		userColectionService.save(userColection);
		addMessage(redirectAttributes, "保存收藏成功");
		return "redirect:"+Global.getAdminPath()+"/star/userColection/?repage";
	}
	
	@RequiresPermissions("star:userColection:edit")
	@RequestMapping(value = "delete")
	public String delete(UserColection userColection, RedirectAttributes redirectAttributes) {
		userColectionService.delete(userColection);
		addMessage(redirectAttributes, "删除收藏成功");
		return "redirect:"+Global.getAdminPath()+"/star/userColection/?repage";
	}

}