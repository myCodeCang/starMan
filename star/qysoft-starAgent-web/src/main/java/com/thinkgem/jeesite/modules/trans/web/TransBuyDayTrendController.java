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
import com.thinkgem.jeesite.modules.trans.entity.TransBuyDayTrend;
import com.thinkgem.jeesite.modules.trans.service.TransBuyDayTrendService;
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
 * 交易历史价格表Controller
 * @author luo
 * @version 2017-05-10
 */
@Controller
@RequestMapping(value = "${adminPath}/trans/transBuyDayTrend")
public class TransBuyDayTrendController extends BaseController {

	@Autowired
	private TransBuyDayTrendService transBuyDayTrendService;
	
	@ModelAttribute
	public TransBuyDayTrend get(@RequestParam(required=false) String id) {
		TransBuyDayTrend entity = null;
		if (StringUtils2.isNotBlank(id)){
			entity = transBuyDayTrendService.get(id);
		}
		if (entity == null){
			entity = new TransBuyDayTrend();
		}
		return entity;
	}
	
	@RequiresPermissions("user:transBuyDayTrend:view")
	@RequestMapping(value = {"list", ""})
	public String list(TransBuyDayTrend transBuyDayTrend, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TransBuyDayTrend> page = transBuyDayTrendService.findPage(new Page<TransBuyDayTrend>(request, response), transBuyDayTrend);
		model.addAttribute("page", page);
		return "modules/trans/transBuyDayTrendList";
	}

	@RequiresPermissions("user:transBuyDayTrend:view")
	@RequestMapping(value = "form")
	public String form(TransBuyDayTrend transBuyDayTrend, Model model) {
		model.addAttribute("transBuyDayTrend", transBuyDayTrend);
		return "modules/trans/transBuyDayTrendForm";
	}

	@RequiresPermissions("user:transBuyDayTrend:edit")
	@RequestMapping(value = "save")
	@RecordLog(logTitle = "艺术品管理-艺术品价格报表-修改")
	public String save(TransBuyDayTrend transBuyDayTrend, Model model, RedirectAttributes redirectAttributes)  {


		try {
			transBuyDayTrendService.save(transBuyDayTrend);
			addMessage(redirectAttributes, "保存交易历史价格成功");
			return "redirect:"+ Global.getAdminPath()+"/trans/transBuyDayTrend/?repage";
		} catch (ValidationException e) {
			addMessage(model, "失败:"+e.getMessage());
			return form(transBuyDayTrend, model);
		} catch (Exception e) {
			addMessage(model,"失败");
			return form(transBuyDayTrend, model);
		}


	}
	
	@RequiresPermissions("user:transBuyDayTrend:edit")
	@RequestMapping(value = "delete")
	public String delete(TransBuyDayTrend transBuyDayTrend, RedirectAttributes redirectAttributes) {

		try {
			transBuyDayTrendService.delete(transBuyDayTrend);
			addMessage(redirectAttributes, "删除交易历史价格成功");
		} catch (ValidationException e) {
			addMessage(redirectAttributes, "失败:"+e.getMessage());
		} catch (Exception e) {
			addMessage(redirectAttributes,"失败");
		}
		return "redirect:"+ Global.getAdminPath()+"/trans/transBuyDayTrend/?repage";
	}

	/**
	 * 导出用户数据
	 * @param transBuyDayTrend
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("user:userUserinfo:view")
	@RequestMapping(value = "export", method= RequestMethod.POST)
	@RecordLog(logTitle = "艺术品管理-艺术品价格报表-导出")
	public String exportFile(TransBuyDayTrend transBuyDayTrend, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "艺术品价格报表数据"+ DateUtils2.getDate("yyyyMMddHHmmss")+".xlsx";
			Page<TransBuyDayTrend> page = transBuyDayTrendService.findPage(new Page<TransBuyDayTrend>(request, response, -1), transBuyDayTrend);
			new ExportExcel("艺术品价格报表数据", TransBuyDayTrend.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/trans/transBuyDayTrend/list";
	}

}