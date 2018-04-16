/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.md.entity.MdTradeMain;
import com.thinkgem.jeesite.modules.md.service.MdTradeMainService;
import com.thinkgem.jeesite.modules.star.entity.EverydayHold;
import com.thinkgem.jeesite.modules.star.service.EverydayHoldService;
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
import java.math.BigDecimal;

/**
 * 日均持仓Controller
 * @author xue
 * @version 2017-10-10
 */
@Controller
@RequestMapping(value = "${adminPath}/star/everydayHold")
public class EverydayHoldController extends BaseController {

	@Autowired
	private EverydayHoldService everydayHoldService;
	@Autowired
	private MdTradeMainService mdTradeMainService;
	
	@ModelAttribute
	public EverydayHold get(@RequestParam(required=false) String id) {
		EverydayHold entity = null;
		if (StringUtils2.isNotBlank(id)){
			entity = everydayHoldService.get(id);
		}
		if (entity == null){
			entity = new EverydayHold();
		}
		return entity;
	}
	
	@RequiresPermissions("user:everydayHold:view")
	@RequestMapping(value = {"list", ""})
	public String list(EverydayHold everydayHold, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<EverydayHold> page = everydayHoldService.findPage(new Page<EverydayHold>(request, response), everydayHold);
		model.addAttribute("page", page);
		return "modules/star/everydayHoldList";
	}

	@RequiresPermissions("user:everydayHold:view")
	@RequestMapping(value = "form")
	public String form(EverydayHold everydayHold, Model model) {
		model.addAttribute("everydayHold", everydayHold);
		return "modules/star/everydayHoldForm";
	}

	@RequiresPermissions("star:everydayHold:edit")
	@RequestMapping(value = "save")
	public String save(EverydayHold everydayHold, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, everydayHold)){
			return form(everydayHold, model);
		}
		everydayHoldService.save(everydayHold);
		addMessage(redirectAttributes, "保存日均持仓成功");
		return "redirect:"+Global.getAdminPath()+"/star/everydayHold/?repage";
	}
	
	@RequiresPermissions("star:everydayHold:edit")
	@RequestMapping(value = "delete")
	public String delete(EverydayHold everydayHold, RedirectAttributes redirectAttributes) {
		everydayHoldService.delete(everydayHold);
		addMessage(redirectAttributes, "删除日均持仓成功");
		return "redirect:"+Global.getAdminPath()+"/star/everydayHold/?repage";
	}

}