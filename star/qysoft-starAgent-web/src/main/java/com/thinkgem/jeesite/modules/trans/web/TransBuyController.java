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
import com.thinkgem.jeesite.modules.trans.entity.TransBuy;
import com.thinkgem.jeesite.modules.trans.service.TransBuyService;
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
 * 艺术品交易大厅Controller
 * @author luo
 * @version 2017-05-10
 */
@Controller
@RequestMapping(value = "${adminPath}/trans/transBuy")
public class TransBuyController extends BaseController {

	@Autowired
	private TransBuyService transBuyService;
	
	@ModelAttribute
	public TransBuy get(@RequestParam(required=false) String id) {
		TransBuy entity = null;
		if (StringUtils2.isNotBlank(id)){
			entity = transBuyService.get(id);
		}
		if (entity == null){
			entity = new TransBuy();
		}
		return entity;
	}



	@RequiresPermissions("user:transBuy:view")
	@RequestMapping(value = {"list", ""})
	public String list(TransBuy transBuy, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TransBuy> page = transBuyService.findPage(new Page<TransBuy>(request, response), transBuy);
		model.addAttribute("page", page);
		return "modules/trans/transBuyList";
	}

	@RequiresPermissions("user:transBuy:view")
	@RequestMapping(value = "form")
	public String form(TransBuy transBuy, Model model) {
		model.addAttribute("transBuy", transBuy);
		return "modules/trans/transBuyForm";
	}

	@RequiresPermissions("user:transBuy:edit")
	@RequestMapping(value = "save")
	public String save(TransBuy transBuy, Model model, RedirectAttributes redirectAttributes)  {

		try {
			transBuyService.save(transBuy);
			addMessage(redirectAttributes, "保存艺术品交易成功");
			return "redirect:"+ Global.getAdminPath()+"/trans/transBuy/?repage";
		} catch (ValidationException e) {
			addMessage(model, "失败:"+e.getMessage());
			return form(transBuy, model);
		} catch (Exception e) {
			addMessage(model,"失败");
			return form(transBuy, model);
		}


	}
	
	@RequiresPermissions("user:transBuy:edit")
	@RequestMapping(value = "delete")
	public String delete(TransBuy transBuy, RedirectAttributes redirectAttributes) {

		try {

			transBuyService.delete(transBuy);
			addMessage(redirectAttributes, "删除艺术品交易成功");
		} catch (ValidationException e) {
			addMessage(redirectAttributes, "失败:"+e.getMessage());
		} catch (Exception e) {
			addMessage(redirectAttributes,"失败");
		}

		return "redirect:"+ Global.getAdminPath()+"/trans/transBuy/?repage";
	}

	/**
	 * 导出用户数据
	 * @param transBuy
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("user:userUserinfo:view")
	@RequestMapping(value = "export", method= RequestMethod.POST)
	@RecordLog(logTitle = "艺术品管理-艺术品交易大厅-导出")
	public String exportFile(TransBuy transBuy, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "艺术品交易大厅数据"+ DateUtils2.getDate("yyyyMMddHHmmss")+".xlsx";
			Page<TransBuy> page = transBuyService.findPage(new Page<TransBuy>(request, response, -1), transBuy);
			new ExportExcel("艺术品交易大厅数据", TransBuy.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/trans/transBuy/list";
	}

}