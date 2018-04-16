/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.web;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils2;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.utils.annotations.RecordLog;
import com.thinkgem.jeesite.modules.trans.entity.TransBuyLog;
import com.thinkgem.jeesite.modules.trans.service.TransBuyLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户交易表Controller
 * @author xueyuliang
 * @version 2017-05-08
 */
@Controller
@RequestMapping(value = "${adminPath}/trans/transBuyLog")
public class TransBuyLogController extends BaseController {

	@Autowired
	private TransBuyLogService transBuyLogService;
	
	@ModelAttribute
	public TransBuyLog get(@RequestParam(required=false) String id) {
		TransBuyLog entity = null;
		if (StringUtils2.isNotBlank(id)){
			entity = transBuyLogService.get(id);
		}
		if (entity == null){
			entity = new TransBuyLog();
		}
		return entity;
	}
	
	@RequiresPermissions("user:transBuyLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(TransBuyLog transBuyLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TransBuyLog> page = transBuyLogService.findPage(new Page<TransBuyLog>(request, response), transBuyLog);
		model.addAttribute("page", page);
		return "modules/trans/transBuyLogList";
	}

	@RequiresPermissions("user:transBuyLog:view")
	@RequestMapping(value = "form")
	public String form(TransBuyLog transBuyLog, Model model) {
		model.addAttribute("transBuyLog", transBuyLog);
		return "modules/trans/transBuyLogForm";
	}

	@RequiresPermissions("user:transBuyLog:edit")
	@RequestMapping(value = "save")
	@RecordLog(logTitle = "艺术品管理-艺术品成交记录-新增/修改")
	public String save(TransBuyLog transBuyLog, Model model, RedirectAttributes redirectAttributes){
		try {
			transBuyLog.setBuyId("0");
			transBuyLogService.save(transBuyLog);
			addMessage(redirectAttributes, "保存用户交易订单成功");
			return "redirect:"+ Global.getAdminPath()+"/trans/transBuyLog/?repage";
		} catch (ValidationException e) {
			addMessage(model, "失败:"+e.getMessage());
			return form(transBuyLog, model);
		} catch (Exception e) {
			addMessage(model,"失败");
			return form(transBuyLog, model);
		}


	}
	
	@RequiresPermissions("user:transBuyLog:edit")
	@RequestMapping(value = "delete")
	public String delete(TransBuyLog transBuyLog, RedirectAttributes redirectAttributes) {

		try {

			transBuyLogService.delete(transBuyLog);
			addMessage(redirectAttributes, "删除用户交易订单成功");
		} catch (ValidationException e) {
			addMessage(redirectAttributes, "失败:"+e.getMessage());
		} catch (Exception e) {
			addMessage(redirectAttributes,"失败");
		}

		return "redirect:"+ Global.getAdminPath()+"/trans/transBuyLog/?repage";
	}

	/**
	 * 导出用户数据
	 * @param transBuyLog
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("user:transBuyLog:view")
	@RequestMapping(value = "export", method= RequestMethod.POST)
	@RecordLog(logTitle = "艺术品管理-艺术品成交记录-导出")
	public String exportFile(TransBuyLog transBuyLog, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "艺术品成交数据"+ DateUtils2.getDate("yyyyMMddHHmmss")+".xlsx";
			Page<TransBuyLog> page = transBuyLogService.findPage(new Page<TransBuyLog>(request, response, -1), transBuyLog);
			new ExportExcel("艺术品成交数据", TransBuyLog.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出数据失败！失败信息："+e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/trans/transBuyLog/list";
	}

}