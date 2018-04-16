/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.web;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.trans.entity.TransApplyCondition;
import com.thinkgem.jeesite.modules.trans.service.TransApplyConditionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 订货限制Controller
 * @author xueyuliang
 * @version 2017-05-09
 */
@Controller
@RequestMapping(value = "${adminPath}/trans/transApplyCondition")
public class TransApplyConditionController extends BaseController {

	@Autowired
	private TransApplyConditionService transApplyConditionService;
	
	@ModelAttribute
	public TransApplyCondition get(@RequestParam(required=false) String id) {
		TransApplyCondition entity = null;
		if (StringUtils2.isNotBlank(id)){
			entity = transApplyConditionService.get(id);
		}
		if (entity == null){
			entity = new TransApplyCondition();
		}
		return entity;
	}
	
	@RequiresPermissions("user:transApplyCondition:view")
	@RequestMapping(value = {"list", ""})
	public String list(TransApplyCondition transApplyCondition, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TransApplyCondition> page = transApplyConditionService.findPage(new Page<TransApplyCondition>(request, response), transApplyCondition);
		model.addAttribute("page", page);
		return "modules/trans/transApplyConditionList";
	}

	@RequiresPermissions("user:transApplyCondition:view")
	@RequestMapping(value = "form")
	public String form(TransApplyCondition transApplyCondition, Model model) {
		model.addAttribute("transApplyCondition", transApplyCondition);
		return "modules/trans/transApplyConditionForm";
	}


}
