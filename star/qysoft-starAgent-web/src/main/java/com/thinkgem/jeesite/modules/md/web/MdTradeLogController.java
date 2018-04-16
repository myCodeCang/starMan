/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.md.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.common.utils.DateUtils2;
import com.thinkgem.jeesite.modules.sys.utils.UserInfoUtils;
import com.thinkgem.jeesite.modules.user.entity.UserUserinfo;
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
import com.thinkgem.jeesite.modules.md.entity.MdTradeLog;
import com.thinkgem.jeesite.modules.md.service.MdTradeLogService;

import java.util.Date;

/**
 * 撮合记录表Controller
 * @author xue
 * @version 2017-10-15
 */
@Controller
@RequestMapping(value = "${adminPath}/md/mdTradeLog")
public class MdTradeLogController extends BaseController {

	@Autowired
	private MdTradeLogService mdTradeLogService;
	
	@ModelAttribute
	public MdTradeLog get(@RequestParam(required=false) String id) {
		MdTradeLog entity = null;
		if (StringUtils2.isNotBlank(id)){
			entity = mdTradeLogService.get(id);
		}
		if (entity == null){
			entity = new MdTradeLog();
		}
		return entity;
	}
	
	@RequiresPermissions("user:mdTradeLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(MdTradeLog mdTradeLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MdTradeLog> page = mdTradeLogService.findPage(new Page<MdTradeLog>(request, response), mdTradeLog); 
		model.addAttribute("page", page);
		return "modules/md/mdTradeLogList";
	}

	@RequiresPermissions("user:mdTradeLog:view")
	@RequestMapping(value ="profitReport")
	public String profitReport(MdTradeLog mdTradeLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		MdTradeLog profitByDate = mdTradeLogService.getProfitByDate(mdTradeLog.getCreateDateBegin(),mdTradeLog.getCreateDateEnd());
		String allProfit;
		if(profitByDate!=null){
			allProfit = profitByDate.getSumProfit();
		}else{
			allProfit = "--";
		}
		model.addAttribute("sumProfit", allProfit);
		if (StringUtils2.isNotBlank(mdTradeLog.getCreateDateBegin()) && StringUtils2.isNotBlank(mdTradeLog.getCreateDateEnd())) {
			model.addAttribute("createDateBegin", DateUtils2.parseDate(mdTradeLog.getCreateDateBegin()) );
			model.addAttribute("createDateEnd", DateUtils2.parseDate(mdTradeLog.getCreateDateEnd()));
		}
		return "modules/md/profitReportList";
	}

	@RequiresPermissions("user:mdTradeLog:view")
	@RequestMapping(value ="userVolumeReport")
	public String userVolumeReport(MdTradeLog mdTradeLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Date beginDate = DateUtils2.parseDate(mdTradeLog.getCreateDateBegin());
		String userName = request.getParameter("userName");
		Date endDate = DateUtils2.parseDate(mdTradeLog.getCreateDateEnd());
		UserUserinfo userUserinfo = new UserUserinfo();
		userUserinfo.setUserName(userName);
		Page<UserUserinfo> page = mdTradeLogService.findAgentVolume(new Page<UserUserinfo>(request, response), mdTradeLog,userUserinfo );
		model.addAttribute("page", page);
		model.addAttribute("createDateBegin",beginDate );
		model.addAttribute("createDateEnd",  endDate);
		return "modules/md/userVolumeReportList";
	}

	@RequiresPermissions("user:mdTradeLog:view")
	@RequestMapping(value = "form")
	public String form(MdTradeLog mdTradeLog, Model model) {
		model.addAttribute("mdTradeLog", mdTradeLog);
		return "modules/md/mdTradeLogForm";
	}

	@RequiresPermissions("md:mdTradeLog:edit")
	@RequestMapping(value = "save")
	public String save(MdTradeLog mdTradeLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mdTradeLog)){
			return form(mdTradeLog, model);
		}
		mdTradeLogService.save(mdTradeLog);
		addMessage(redirectAttributes, "保存撮合记录成功");
		return "redirect:"+Global.getAdminPath()+"/md/mdTradeLog/?repage";
	}
	
	@RequiresPermissions("md:mdTradeLog:edit")
	@RequestMapping(value = "delete")
	public String delete(MdTradeLog mdTradeLog, RedirectAttributes redirectAttributes) {
		mdTradeLogService.delete(mdTradeLog);
		addMessage(redirectAttributes, "删除撮合记录成功");
		return "redirect:"+Global.getAdminPath()+"/md/mdTradeLog/?repage";
	}

}