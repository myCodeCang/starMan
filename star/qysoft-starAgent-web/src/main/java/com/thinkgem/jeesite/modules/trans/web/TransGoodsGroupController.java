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
import com.thinkgem.jeesite.modules.trans.entity.TransGoodsGroup;
import com.thinkgem.jeesite.modules.trans.service.TransGoodsGroupService;
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
 * 艺术品委托管理Controller
 * @author luo
 * @version 2017-05-10
 */
@Controller
@RequestMapping(value = "${adminPath}/trans/transGoodsGroup")
public class TransGoodsGroupController extends BaseController {

	@Autowired
	private TransGoodsGroupService transGoodsGroupService;

	@ModelAttribute
	public TransGoodsGroup get(@RequestParam(required=false) String id) {
		TransGoodsGroup entity = null;
		if (StringUtils2.isNotBlank(id)){
			entity = transGoodsGroupService.get(id);
		}
		if (entity == null){
			entity = new TransGoodsGroup();
		}
		return entity;
	}

	@RequiresPermissions("user:transGoodsGroup:view")
	@RequestMapping(value = {"list", ""})
	public String list(TransGoodsGroup transGoodsGroup, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TransGoodsGroup> page = transGoodsGroupService.findPage(new Page<TransGoodsGroup>(request, response), transGoodsGroup);
		model.addAttribute("page", page);
		return "modules/trans/transGoodsGroupList";
	}

	@RequiresPermissions("user:transGoodsGroup:view")
	@RequestMapping(value = "form")
	public String form(TransGoodsGroup transGoodsGroup, Model model) {
		model.addAttribute("transGoodsGroup", transGoodsGroup);
		return "modules/trans/transGoodsGroupForm";
	}

	@RequiresPermissions("user:transGoodsGroup:edit")
	@RequestMapping(value = "save")
	@RecordLog(logTitle = "艺术品管理-艺术品委托管理-新增/修改")
	public String save(TransGoodsGroup transGoodsGroup, Model model, RedirectAttributes redirectAttributes) throws ValidationException {
		try {
			transGoodsGroupService.save(transGoodsGroup);
			addMessage(redirectAttributes, "保存艺术品成功");
			return "redirect:"+ Global.getAdminPath()+"/trans/transGoodsGroup/?repage";
		} catch (ValidationException e) {
			addMessage(model, "失败:"+e.getMessage());
			return form(transGoodsGroup, model);
		}
	}
	
	@RequiresPermissions("user:transGoodsGroup:edit")
	@RequestMapping(value = "delete")
	@RecordLog(logTitle = "艺术品管理-艺术品委托管理-删除")
	public String delete(TransGoodsGroup transGoodsGroup, RedirectAttributes redirectAttributes)throws ValidationException {
		try {
			transGoodsGroupService.delete(transGoodsGroup);
			addMessage(redirectAttributes, "删除艺术品成功");
			return "redirect:"+ Global.getAdminPath()+"/trans/transGoodsGroup/?repage";
		} catch (ValidationException e) {
			addMessage(redirectAttributes, "失败:"+e.getMessage());
			return "redirect:"+ Global.getAdminPath()+"/trans/transGoodsGroup/?repage";
		}
	}

	@RequiresPermissions("user:transGoodsGroup:view")
	@RequestMapping(value = "selectTrans")
	public String selectTrans(TransGoodsGroup transGoodsGroup, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TransGoodsGroup> page = transGoodsGroupService.findPage(new Page<TransGoodsGroup>(request, response), transGoodsGroup);
		model.addAttribute("page", page);
		return "modules/user/selectGoodsGroupTagList";
	}

	/**
	 * 导出用户数据
	 * @param transGoodsGroup
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("user:transGoodsGroup:view")
	@RequestMapping(value = "export", method= RequestMethod.POST)
	@RecordLog(logTitle = "艺术品管理-艺术品委托管理-导出")
	public String exportFile(TransGoodsGroup transGoodsGroup, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "艺术品委托信息"+ DateUtils2.getDate("yyyyMMddHHmmss")+".xlsx";
			Page<TransGoodsGroup> page = transGoodsGroupService.findPage(new Page<TransGoodsGroup>(request, response, -1), transGoodsGroup);
			new ExportExcel("艺术品委托信息", TransGoodsGroup.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出数据失败！失败信息："+e.getMessage());
		}
		return "redirect:"+ Global.getAdminPath()+"/trans/transGoodsGroup/list";
	}

}