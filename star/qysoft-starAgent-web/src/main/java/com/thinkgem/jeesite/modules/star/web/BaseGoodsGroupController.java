/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.star.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.star.entity.BaseGoodsGroup;
import com.thinkgem.jeesite.modules.star.entity.ShopClassify;
import com.thinkgem.jeesite.modules.star.service.BaseGoodsGroupService;
import com.thinkgem.jeesite.modules.star.service.ShopClassifyService;
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
import java.util.List;

/**
 * 基础商品Controller
 * @author luojie
 * @version 2017-09-25
 */
@Controller
@RequestMapping(value = "${adminPath}/star/baseGoodsGroup")
public class BaseGoodsGroupController extends BaseController {

	@Autowired
	private BaseGoodsGroupService baseGoodsGroupService;
	@Autowired
	private ShopClassifyService shopClassifyService;
	
	@ModelAttribute
	public BaseGoodsGroup get(@RequestParam(required=false) String id) {
		BaseGoodsGroup entity = null;
		if (StringUtils2.isNotBlank(id)){
			entity = baseGoodsGroupService.get(id);
		}
		if (entity == null){
			entity = new BaseGoodsGroup();
		}
		return entity;
	}
	
	@RequiresPermissions("user:baseGoodsGroup:view")
	@RequestMapping(value = {"list", ""})
	public String list(BaseGoodsGroup baseGoodsGroup, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BaseGoodsGroup> page = baseGoodsGroupService.findPage(new Page<BaseGoodsGroup>(request, response), baseGoodsGroup); 
		model.addAttribute("page", page);
		return "modules/star/baseGoodsGroupList";
	}

	@RequiresPermissions("user:baseGoodsGroup:view")
	@RequestMapping(value = "form")
	public String form(BaseGoodsGroup baseGoodsGroup, Model model) {
		List<ShopClassify> categoryList = shopClassifyService.findList(new ShopClassify());
		model.addAttribute("categoryList",categoryList);
		model.addAttribute("baseGoodsGroup", baseGoodsGroup);
		return "modules/star/baseGoodsGroupForm";
	}

	@RequiresPermissions("user:baseGoodsGroup:edit")
	@RequestMapping(value = "save")
	public String save(BaseGoodsGroup baseGoodsGroup, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, baseGoodsGroup)){
			return form(baseGoodsGroup, model);
		}
		try {
			baseGoodsGroupService.save(baseGoodsGroup);
		} catch (Exception e) {
			addMessage(model, "失败:"+e.getMessage());
			return form(baseGoodsGroup, model);
		}
		addMessage(redirectAttributes, "保存明星产品成功");
		return "redirect:"+Global.getAdminPath()+"/star/baseGoodsGroup/?repage";
	}
	
	@RequiresPermissions("user:baseGoodsGroup:edit")
	@RequestMapping(value = "delete")
	public String delete(BaseGoodsGroup baseGoodsGroup, RedirectAttributes redirectAttributes) {
		baseGoodsGroupService.delete(baseGoodsGroup);
		addMessage(redirectAttributes, "删除明星产品成功");
		return "redirect:"+Global.getAdminPath()+"/star/baseGoodsGroup/?repage";
	}

	@RequestMapping(value = "selectGoods")
	public String selectGoods(BaseGoodsGroup baseGoodsGroup, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<BaseGoodsGroup> page = baseGoodsGroupService.findPage(new Page<BaseGoodsGroup>(request, response), baseGoodsGroup);
		model.addAttribute("page", page);

		return "modules/star/selectBaseGoodsGroupTagList";
	}

}