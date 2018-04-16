/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.user.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.user.entity.UserLevel;
import com.thinkgem.jeesite.modules.user.entity.WorkProject;
import com.thinkgem.jeesite.modules.user.service.UserLevelService;
import com.thinkgem.jeesite.modules.user.service.WorkProjectService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 项目Controller
 * @author luo
 * @version 2017-08-04
 */
@Controller
@RequestMapping(value = "${adminPath}/user/workProject")
public class WorkProjectController extends BaseController {
	@Resource
	private UserLevelService userLevelService;
	@Autowired
	private WorkProjectService workProjectService;
	
	@ModelAttribute
	public WorkProject get(@RequestParam(required=false) String id) {
		WorkProject entity = null;
		if (StringUtils2.isNotBlank(id)){
			entity = workProjectService.get(id);
		}
		if (entity == null){
			entity = new WorkProject();
		}
		return entity;
	}
	
	@RequiresPermissions("user:workProject:view")
	@RequestMapping(value = {"list", ""})
	public String list(WorkProject workProject, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WorkProject> page = workProjectService.findPage(new Page<WorkProject>(request, response), workProject);
		model.addAttribute("page", page);
		return "modules/user/workProjectList";
	}

	@RequiresPermissions("user:workProject:view")
	@RequestMapping(value = "form")
	public String form(WorkProject workProject, Model model) {
		List<UserLevel> userLevels = userLevelService.findList(new UserLevel());
		model.addAttribute("userLevels", userLevels);
		model.addAttribute("workProject", workProject);
		return "modules/user/workProjectForm";
	}

	@RequiresPermissions("user:workProject:edit")
	@RequestMapping(value = "save")
	public String save(WorkProject workProject, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		if (!beanValidator(model, workProject)){
			return form(workProject, model);
		}
		//上传图片地址替换为绝对路径
		if (workProject != null) {
			String detail = workProject.getDetail();
			if (!StringUtils2.isBlank(detail)) {
				StringBuilder url = new StringBuilder();
				url.append("src=&quot;");
				url.append(request.getScheme()).append("://").append(request.getServerName())
						.append(":").append(request.getServerPort()).append("/").append(request.getContextPath());
				String newContent = detail.replace("src=&quot;/userfiles", url.append("/userfiles"));
				workProject.setDetail(newContent);
			}
		}
		workProjectService.save(workProject);
		addMessage(redirectAttributes, "保存项目成功");
		return "redirect:"+ Global.getAdminPath()+"/user/workProject/?repage";
	}
	
	@RequiresPermissions("user:workProject:edit")
	@RequestMapping(value = "delete")
	public String delete(WorkProject workProject, RedirectAttributes redirectAttributes) {
		workProjectService.delete(workProject);
		addMessage(redirectAttributes, "删除项目成功");
		return "redirect:"+ Global.getAdminPath()+"/user/workProject/?repage";
	}

	@RequiresPermissions("user:workProject:view")
	@RequestMapping(value = "sellOutStatus")
	public String sellOut(HttpServletRequest request,WorkProject workProject, Model model) {

		String status = request.getParameter("status");

		if(StringUtils2.isNotBlank(status)){
			workProject.setStatue(status);
			workProjectService.sellOutStatus(workProject);
		}
		return "redirect:"+ Global.getAdminPath()+"/user/workProject/?repage";
	}

}