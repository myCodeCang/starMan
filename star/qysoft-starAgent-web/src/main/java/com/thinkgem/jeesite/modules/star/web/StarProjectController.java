/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.star.entity.StarProject;
import com.thinkgem.jeesite.modules.star.service.StarProjectService;
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
 * 明星产品Controller
 * @author luo
 * @version 2017-10-18
 */
@Controller
@RequestMapping(value = "${adminPath}/star/starProject")
public class StarProjectController extends BaseController {

	@Autowired
	private StarProjectService starProjectService;
	
	@ModelAttribute
	public StarProject get(@RequestParam(required=false) String id) {
		StarProject entity = null;
		if (StringUtils2.isNotBlank(id)){
			entity = starProjectService.get(id);
		}
		if (entity == null){
			entity = new StarProject();
		}
		return entity;
	}
	
	@RequiresPermissions("user:starProject:view")
	@RequestMapping(value = {"list", ""})
	public String list(StarProject starProject, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<StarProject> page = starProjectService.findPage(new Page<StarProject>(request, response), starProject); 
		model.addAttribute("page", page);
		return "modules/star/starProjectList";
	}

	@RequiresPermissions("user:starProject:view")
	@RequestMapping(value = "form")
	public String form(StarProject starProject, Model model) {
		model.addAttribute("starProject", starProject);
		return "modules/star/starProjectForm";
	}

	@RequiresPermissions("user:starProject:edit")
	@RequestMapping(value = "save")
	public String save(StarProject starProject, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, starProject)){
			return form(starProject, model);
		}
		try {
			starProjectService.save(starProject);
		} catch (Exception e) {
			addMessage(model, "失败"+e.getMessage());
			return form(starProject, model);
		}
		addMessage(redirectAttributes, "保存明星产品成功");
		return "redirect:"+Global.getAdminPath()+"/star/starProject/?repage";
	}
	
	@RequiresPermissions("user:starProject:edit")
	@RequestMapping(value = "delete")
	public String delete(StarProject starProject, RedirectAttributes redirectAttributes) {
		starProjectService.delete(starProject);
		addMessage(redirectAttributes, "删除明星产品成功");
		return "redirect:"+Global.getAdminPath()+"/star/starProject/?repage";
	}

}