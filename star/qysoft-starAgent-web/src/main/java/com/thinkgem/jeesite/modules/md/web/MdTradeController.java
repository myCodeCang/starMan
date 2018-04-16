/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.md.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.md.entity.MdTrade;
import com.thinkgem.jeesite.modules.md.service.MdTradeService;
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
import java.util.Date;

/**
 * 交易表Controller
 * @author xue
 * @version 2017-10-15
 */
@Controller
@RequestMapping(value = "${adminPath}/md/mdTrade")
public class MdTradeController extends BaseController {

	@Autowired
	private MdTradeService mdTradeService;
	
	@ModelAttribute
	public MdTrade get(@RequestParam(required=false) String id) {
		MdTrade entity = null;
		if (StringUtils2.isNotBlank(id)){
			entity = mdTradeService.get(id);
		}
		if (entity == null){
			entity = new MdTrade();
		}
		return entity;
	}
	
	@RequiresPermissions("user:mdTrade:view")
	@RequestMapping(value = {"list", ""})
	public String list(MdTrade mdTrade, HttpServletRequest request, HttpServletResponse response, Model model) {
		mdTrade.setCreateDate(new Date());
		Page<MdTrade> page = mdTradeService.findPage(new Page<MdTrade>(request, response), mdTrade); 
		model.addAttribute("page", page);
		return "modules/md/mdTradeList";
	}

	@RequiresPermissions("user:mdTrade:view")
	@RequestMapping(value = "form")
	public String form(MdTrade mdTrade, Model model) {
		model.addAttribute("mdTrade", mdTrade);
		return "modules/md/mdTradeForm";
	}

	@RequiresPermissions("user:mdTrade:edit")
	@RequestMapping(value = "save")
	public String save(MdTrade mdTrade, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mdTrade)){
			return form(mdTrade, model);
		}
		mdTradeService.save(mdTrade);
		addMessage(redirectAttributes, "保存交易成功");
		return "redirect:"+Global.getAdminPath()+"/md/mdTrade/?repage";
	}
	
	@RequiresPermissions("user:mdTrade:edit")
	@RequestMapping(value = "delete")
	public String delete(MdTrade mdTrade, RedirectAttributes redirectAttributes) {
		mdTradeService.delete(mdTrade);
		addMessage(redirectAttributes, "删除交易成功");
		return "redirect:"+Global.getAdminPath()+"/md/mdTrade/?repage";
	}

}