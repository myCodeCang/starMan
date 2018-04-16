/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.web;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils2;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.md.entity.GoodsStatus;
import com.thinkgem.jeesite.modules.md.entity.MdTradeMain;
import com.thinkgem.jeesite.modules.md.service.MdTradeMainService;
import com.thinkgem.jeesite.modules.star.dao.RealTimePriceDao;
import com.thinkgem.jeesite.modules.star.entity.BaseGoodsGroup;
import com.thinkgem.jeesite.modules.star.entity.RealTimePrice;
import com.thinkgem.jeesite.modules.star.service.BaseGoodsGroupService;
import com.thinkgem.jeesite.modules.sys.utils.annotations.RecordLog;
import com.thinkgem.jeesite.modules.trans.entity.TransGoods;
import com.thinkgem.jeesite.modules.trans.entity.TransGoodsKuiSun;
import com.thinkgem.jeesite.modules.trans.entity.TransGoodsNumReport;
import com.thinkgem.jeesite.modules.trans.service.TransGoodsService;
import com.thinkgem.jeesite.modules.user.entity.UserUserinfo;
import com.thinkgem.jeesite.modules.user.service.UserUserinfoService;
import org.apache.ibatis.annotations.Param;
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
import java.util.Date;

/**
 * 交易品持仓Controller
 *
 * @author luo
 * @version 2017-05-10
 */
@Controller
@RequestMapping(value = "${adminPath}/trans/transGoods")
public class TransGoodsController extends BaseController {

    @Autowired
    private TransGoodsService transGoodsService;
    @Autowired
    private BaseGoodsGroupService baseGoodsGroupService;
    @Autowired
    private UserUserinfoService userUserinfoService;
    @Autowired
    private MdTradeMainService mdTradeMainService;
    @Autowired
    private RealTimePriceDao realTimePriceDao;

    @ModelAttribute
    public TransGoods get(@RequestParam(required = false) String id) {
        TransGoods entity = null;
        if (StringUtils2.isNotBlank(id)) {
            entity = transGoodsService.get(id);
        }
        if (entity == null) {
            entity = new TransGoods();
        }
        return entity;
    }

    @RequiresPermissions("user:transGoods:view")
    @RequestMapping(value = {"list", ""})
    public String list(TransGoods transGoods, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<TransGoods> page = transGoodsService.findPage(new Page<TransGoods>(request, response), transGoods);
        model.addAttribute("page", page);
        return "modules/trans/transGoodsList";
    }

    @RequiresPermissions("user:transGoods:view")
    @RequestMapping(value = "form")
    public String form(TransGoods transGoods, Model model) {
        model.addAttribute("transGoods", transGoods);
        return "modules/trans/transGoodsForm";
    }

    @RequiresPermissions("user:transGoods:edit")
    @RequestMapping(value = "save")
    @RecordLog(logTitle = "艺术品管理-艺术品持仓/平仓/提货-新增/修改")
    public String save(TransGoods transGoods, Model model, RedirectAttributes redirectAttributes) throws ValidationException {

        try {
            transGoodsService.save(transGoods);
            addMessage(redirectAttributes, "保存交易品成功");
            return "redirect:" + Global.getAdminPath() + "/trans/transGoods/?repage";
        } catch (ValidationException e) {
            addMessage(model, "失败:"+e.getMessage());
            return form(transGoods, model);
        } catch (Exception e) {
            addMessage(model,"失败");
            return form(transGoods, model);
        }

    }

    @RequiresPermissions("user:transGoods:edit")
    @RequestMapping(value = "delete")
    @RecordLog(logTitle = "艺术品管理-艺术品持仓/平仓/提货-删除")
    public String delete(TransGoods transGoods, RedirectAttributes redirectAttributes)throws ValidationException {
        try {

            transGoodsService.delete(transGoods);
            addMessage(redirectAttributes, "删除交易品成功");
        } catch (ValidationException e) {
            addMessage(redirectAttributes, "失败:"+e.getMessage());
        } catch (Exception e) {
            addMessage(redirectAttributes,"失败");
        }
        return "redirect:" + Global.getAdminPath() + "/trans/transGoods/?repage";
    }

    @RequiresPermissions("user:transGoods:view")
    @RequestMapping(value = "userKunSunRanklist")
    public String userKunSunRanklist(TransGoodsKuiSun transGoods, HttpServletRequest request, HttpServletResponse response, Model model) {

        Page<TransGoodsKuiSun> page = transGoodsService.findKunSunRanklist(new Page<TransGoodsKuiSun>(request, response),transGoods);
        model.addAttribute("page", page);
        return "modules/trans/userKunSunRanklist";
    }

    @RequiresPermissions("user:transGoods:view")
    @RequestMapping(value = "transGoodsNumReport")
    public String transGoodsNumReport(TransGoodsNumReport transGoods, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("goodsNum", transGoodsService.getTransGoodsNumReport(transGoods.getGroupId()));
        return "modules/trans/transGoodsNumReport";
    }

    /**
     * 导出用户数据
     * @param transGoods
     * @param request
     * @param response
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("user:transGoods:view")
    @RequestMapping(value = "export", method= RequestMethod.POST)
    @RecordLog(logTitle = "艺术品管理-艺术品持仓/平仓/提货-导出")
    public String exportFile(TransGoods transGoods, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        String status = request.getParameter("status");
        String title=null;
        if ("2".equals(status)){
             title = "艺术品持仓";
        }else if("0".equals(status)){
            title = "艺术品平仓";
        } else if("4".equals(status)){
            title = "艺术品提货";
        }
        try {
            String fileName = title+ DateUtils2.getDate("yyyyMMddHHmmss")+".xlsx";
            transGoods.setStatus(status);
            Page<TransGoods> page = transGoodsService.findPage(new Page<TransGoods>(request, response, -1), transGoods);
            new ExportExcel(title, TransGoods.class).setDataList(page.getList()).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导出数据失败！失败信息："+e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/trans/transGoods/list";
    }

    @RequestMapping(value = "exports")
    @RecordLog(logTitle = "艺术品管理-艺术品盈利/亏损排行-导出")
    public String exports(TransGoodsKuiSun transGoods, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
        String fileName = "艺术品盈亏数据"+ DateUtils2.getDate("yyyyMMddHHmmss")+".xlsx";
        String order = request.getParameter("order");
        transGoods.setOrder(Integer.parseInt(order));
        Page<TransGoodsKuiSun> page = transGoodsService.findKunSunRanklist(new Page<TransGoodsKuiSun>(request, response),transGoods);
        new ExportExcel("艺术品盈亏数据", TransGoodsKuiSun.class).setDataList(page.getList()).write(response, fileName).dispose();
        return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导出数据失败！失败信息："+e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/trans/transGoods/userKunSunRanklist";
    }
    @RequiresPermissions("user:TransGoods:edit")
    @RequestMapping(value = "publish")
    public String publish(TransGoods transGoods, RedirectAttributes redirectAttributes) {
        return "modules/star/publishForm";
    }
    @RequiresPermissions("user:TransGoods:edit")
    @RequestMapping(value = "saveTransGoods")
    public String saveTransGoods(TransGoods transGoods, Model model) {
        BaseGoodsGroup baseGoodsGroup = baseGoodsGroupService.get(transGoods.getGroupId());
        UserUserinfo byName = userUserinfoService.getByName(transGoods.getUserName());
        if (!transGoods.getGroupId().equals(byName.getUsercenterAddress())){
            addMessage(model,"失败:该承销商未拍卖到该拍卖品,请重新选择!");
            return "modules/star/publishForm";
        }
        if(!GoodsStatus.NO_TRANS.toString().equals(baseGoodsGroup.getStatus())){
            addMessage(model,"失败:该产品已被发布过了!");
            return "modules/star/publishForm";
        }
        baseGoodsGroupService.updateStatusTime(transGoods.getGroupId(),GoodsStatus.MD.toString(),new Date());
        baseGoodsGroupService.updateBaseMoney(transGoods.getGroupId(),transGoods.getBuyMoney());
        baseGoodsGroupService.updateBaseNum(transGoods.getGroupId(),transGoods.getNum());
        transGoods.setStatus(EnumUtil.GoodsType.hold.toString());
        transGoodsService.save(transGoods);
        MdTradeMain mdTradeMain = new MdTradeMain();
        mdTradeMain.setGroupId(transGoods.getGroupId());
        mdTradeMain.setTransactionPrice(transGoods.getBuyMoney());
        mdTradeMain.setOpeningPrice(transGoods.getBuyMoney());
        mdTradeMain.setClosingPrice(transGoods.getBuyMoney());
        mdTradeMain.setHighestPrice(transGoods.getBuyMoney());
        mdTradeMain.setLowestPrice(transGoods.getBuyMoney());
        String week = DateUtils2.getWeekOfDate(new Date());
        if("on".equals(Global.getOption("md_config",week))){
            mdTradeMain.setIsTransDay(EnumUtil.YesNO.YES.toString());
        }else{
            mdTradeMain.setIsTransDay(EnumUtil.YesNO.NO.toString());
        }
        mdTradeMain.setIsOverTop(EnumUtil.YesNO.NO.toString());
        mdTradeMainService.save(mdTradeMain);
        RealTimePrice realTimePrice = new RealTimePrice();
        realTimePrice.setGroupId(transGoods.getGroupId());
        realTimePrice.setPrice(transGoods.getBuyMoney());
        realTimePrice.setCreateDate(new Date());
        realTimePriceDao.insert(realTimePrice);
        addMessage(model,"成功:发布成功!");
        return "modules/star/publishForm";
    }
}