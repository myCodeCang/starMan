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
import com.thinkgem.jeesite.modules.trans.entity.TransOrder;
import com.thinkgem.jeesite.modules.trans.service.TransOrderService;
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
 * 提货订单Controller
 * @author ss
 * @version 2017-05-08
 */
@Controller
@RequestMapping(value = "${adminPath}/trans/transOrder")
public class TransOrderController extends BaseController {

	@Autowired
	private TransOrderService transOrderService;
	
	@ModelAttribute
	public TransOrder get(@RequestParam(required=false) String id) {
		TransOrder entity = null;
		if (StringUtils2.isNotBlank(id)){
			entity = transOrderService.get(id);
		}
		if (entity == null){
			entity = new TransOrder();
		}
		return entity;
	}
	
	@RequiresPermissions("user:transOrder:view")
	@RequestMapping(value = {"list", ""})
	public String list(TransOrder transOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TransOrder> page = transOrderService.findPage(new Page<TransOrder>(request, response), transOrder);
		model.addAttribute("page", page);
		return "modules/trans/transOrderList";
	}

	@RequestMapping(value = "updateType")
	@RecordLog(logTitle = "艺术品管理-艺术品提货管理-发货")
	public String updetType(TransOrder transOrder, Model model, RedirectAttributes redirectAttributes) throws ValidationException {

		try {
			transOrderService.updateType(transOrder);
			addMessage(redirectAttributes, "发货成功");
		} catch (ValidationException e) {
			addMessage(redirectAttributes, "发货失败:"+e.getMessage());
		}
		return "redirect:"+ Global.getAdminPath()+"/trans/transOrder/?repage";
	}

	/**
	 * 导出用户数据
	 * @param transOrder
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("user:transOrder:view")
	@RequestMapping(value = "export", method= RequestMethod.POST)
	@RecordLog(logTitle = "艺术品管理-艺术品提货管理-导出")
	public String exportFile(TransOrder transOrder, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "艺术品提货信息"+ DateUtils2.getDate("yyyyMMddHHmmss")+".xlsx";
			Page<TransOrder> page = transOrderService.findPage(new Page<TransOrder>(request, response, -1), transOrder);
			new ExportExcel("艺术品提货信息", TransOrder.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出数据失败！失败信息："+e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/trans/transOrder/list";
	}


}