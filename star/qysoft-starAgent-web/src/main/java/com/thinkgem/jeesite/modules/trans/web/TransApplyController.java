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
import com.thinkgem.jeesite.modules.trans.entity.AdminApplyTransGoods;
import com.thinkgem.jeesite.modules.trans.entity.TransApply;
import com.thinkgem.jeesite.modules.trans.entity.TransApplyCondition;
import com.thinkgem.jeesite.modules.trans.service.TransApplyConditionService;
import com.thinkgem.jeesite.modules.trans.service.TransApplyService;
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
import java.util.ArrayList;
import java.util.List;

/**
 * 订货表Controller
 * @author xueyuliang
 * @version 2017-05-08
 */
@Controller
@RequestMapping(value = "${adminPath}/trans/transApply")
public class TransApplyController extends BaseController {

	@Autowired
	private TransApplyService transApplyService;
	@Autowired
	private TransApplyConditionService transApplyConditionService;
	
	@ModelAttribute
	public TransApply get(@RequestParam(required=false) String id) {
		TransApply entity = null;
		if (StringUtils2.isNotBlank(id)){
			entity = transApplyService.get(id);
		}
		if (entity == null){
			entity = new TransApply();
		}
		return entity;
	}
	
	@RequiresPermissions("user:transApply:view")
	@RequestMapping(value = {"list", ""})
	public String list(TransApply transApply, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TransApply> page = transApplyService.findPage(new Page<TransApply>(request, response), transApply);
		model.addAttribute("page", page);
		return "modules/trans/transApplyList";
	}

	@RequiresPermissions("user:transApply:view")
	@RequestMapping(value = "form")
	public String form(TransApply transApply, Model model) {
		model.addAttribute("transApply", transApply);
		return "modules/trans/transApplyForm";
	}

	@RequiresPermissions("user:transApply:edit")
	@RequestMapping(value = "save")
	@RecordLog(logTitle = "艺术品管理-艺术品认购管理-新增/修改")
	public String save(TransApply transApply, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes, @RequestParam(required=false) String[]contiditonGroupId, @RequestParam(required=false) String[]contiditonNum) {

		try {

			List<TransApplyCondition> conditionList = new ArrayList<>();
			if(contiditonGroupId != null){
				for (int i = 0; i < contiditonGroupId.length ; i++) {
					TransApplyCondition applyCondition = new TransApplyCondition();
					applyCondition.setApplyId(transApply.getId());
					applyCondition.setHoldGroupId(contiditonGroupId[i]);
					applyCondition.setHoldGroupNum(contiditonNum[i]);

					conditionList.add(applyCondition);

				}
			}

			transApplyService.save(transApply,conditionList);
			addMessage(redirectAttributes, "保存订货表成功");
			return "redirect:"+ Global.getAdminPath()+"/trans/transApply/?repage";
		} catch (ValidationException e) {
			addMessage(model, "失败:"+e.getMessage());
			return form(transApply, model);
		} catch (Exception e) {
			addMessage(model,"失败");
			return form(transApply, model);
		}


	}
	
	@RequiresPermissions("user:transApply:edit")
	@RequestMapping(value = "delete")
	@RecordLog(logTitle = "艺术品管理-艺术品认购管理-删除")
	public String delete(TransApply transApply, RedirectAttributes redirectAttributes) {
		try {

			transApplyService.delete(transApply);
			addMessage(redirectAttributes, "删除订货表成功");
		} catch (ValidationException e) {
			addMessage(redirectAttributes, "失败:"+e.getMessage());
		} catch (Exception e) {
			addMessage(redirectAttributes,"失败");
		}

		return "redirect:"+ Global.getAdminPath()+"/trans/transApply/?repage";
	}

	@RequestMapping(value = "adminApplyTransGoods", method = RequestMethod.GET)
	public String adminApplyTransGoods(Model model) {


		return "modules/trans/adminApplyTransGoods";
	}
	@RequestMapping(value = "adminApplyTransGoods", method = RequestMethod.POST)
	@RecordLog(logTitle = "艺术品管理-手动配发艺术品-新增")
	public String adminApplyTransGoods(AdminApplyTransGoods transApply, Model model, RedirectAttributes redirectAttributes)throws ValidationException {
		try{
			transApplyService.adminApplyTransGoods(transApply);
			addMessage(redirectAttributes, "手动配发成功!");
			return "redirect:" + Global.getAdminPath() + "/trans/transApply/adminApplyTransGoods?repage";
		}catch (ValidationException e){
			addMessage(redirectAttributes, "失败:" + e.getMessage());
			return "redirect:" + Global.getAdminPath() + "/trans/transApply/adminApplyTransGoods?repage";
		}

	}

	/**
	 * 导出用户数据
	 * @param transApply
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("user:transApply:view")
	@RequestMapping(value = "export", method=RequestMethod.POST)
	@RecordLog(logTitle = "艺术品管理-艺术品认购管理-导出")
	public String exportFile(TransApply transApply, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "艺术品认购信息"+ DateUtils2.getDate("yyyyMMddHHmmss")+".xlsx";
			Page<TransApply> page = transApplyService.findPage(new Page<TransApply>(request, response, -1), transApply);
			new ExportExcel("艺术品认购信息", TransApply.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出数据失败！失败信息："+e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/trans/transApply/list";
	}

}