/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.website.webapp;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.security.shiro.session.SessionDAO2;
import com.thinkgem.jeesite.common.utils.*;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.star.entity.AuctionGuarantee;
import com.thinkgem.jeesite.modules.star.entity.SaleByAuction;
import com.thinkgem.jeesite.modules.star.entity.SaleByAuctionLog;
import com.thinkgem.jeesite.modules.star.service.AuctionGuaranteeService;
import com.thinkgem.jeesite.modules.star.service.SaleByAuctionLogService;
import com.thinkgem.jeesite.modules.star.service.SaleByAuctionService;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import com.thinkgem.jeesite.modules.sys.service.DictService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserInfoUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.user.entity.*;
import com.thinkgem.jeesite.modules.user.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

/**
 * 用户个人信息
 *
 * @author ThinkGem
 * @version 2013-5-29
 */
@Controller
@RequestMapping(value = "${frontPath}/app/auction")
public class AppAuctionController extends BaseController {

    @Resource
    private SaleByAuctionService saleByAuctionService;
    @Resource
    private SaleByAuctionLogService saleByAuctionLogService;
    @Resource
    private AuctionGuaranteeService auctionGuaranteeService;


    //查询拍卖列表
    @RequestMapping(value = "/selectAuctionList", method = RequestMethod.POST)
    public String selectAuctionList(HttpServletRequest request, HttpServletResponse response, Model model) {
        String status = request.getParameter("status");
        SaleByAuction saleByAuction = new SaleByAuction();
        saleByAuction.setStatus(status);
        Page<SaleByAuction> sale = saleByAuctionService.findPage(new Page<SaleByAuction>(request, response), saleByAuction);
        model.addAttribute("saleList",sale);
        return success("成功!!", response, model);
    }

    //查询拍卖详情
    @RequestMapping(value = "/auctionInfo", method = RequestMethod.POST)
    public String auctionInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
        String id = request.getParameter("id");
        if(StringUtils2.isBlank(id)){
            return error("查询错误,稍后再试", response, model);
        }
        SaleByAuction byGroupId = saleByAuctionService.get(id);
        model.addAttribute("auctionMoney",Global.getOption("system_trans","auction_money","1000"));
        model.addAttribute("auctionInfo",byGroupId);
        return success("成功!!", response, model);
    }

    //查询拍卖实时数据
    @RequestMapping(value = "/realDataSelect", method = RequestMethod.POST)
    public String realDataSelect(HttpServletRequest request, HttpServletResponse response, Model model) {
        String auctionId = request.getParameter("id");
        String idAfter = request.getParameter("lastId");
        if(StringUtils2.isBlank(auctionId)){
            return error("查询错误,稍后再试", response, model);
        }
        List<SaleByAuctionLog> byAuctionId = saleByAuctionLogService.getByAuctionIdAfterId(auctionId,idAfter);
        model.addAttribute("auction",saleByAuctionService.get(auctionId));
        model.addAttribute("realData",byAuctionId);
        return success("成功!!", response, model);
    }


    //竞价
    @RequestMapping(value = "/makingAnOffer", method = RequestMethod.POST)
    public String makingAnOffer(HttpServletRequest request, HttpServletResponse response, Model model) {
        String auctionId = request.getParameter("id");
        String multiple = request.getParameter("multiple");
        String userName = UserInfoUtils.getUser().getUserName();
        try {
            saleByAuctionService.makingAnOffer(userName,auctionId,multiple);
        } catch (ValidationException e) {
            model.addAttribute("errorCode",e.getErrorCode());
            return error(e.getMessage(), response, model);
        }
        return success("成功!!", response, model);
    }


    //是否交了保证金
    @RequestMapping(value = "/baozhengVerify", method = RequestMethod.POST)
    public String baozhengVerify(HttpServletRequest request, HttpServletResponse response, Model model) {
        String auctionId = request.getParameter("id");
        String userName = UserInfoUtils.getUser().getId();
        boolean pay = false;
        try {
            AuctionGuarantee byNameId = auctionGuaranteeService.findByNameId(userName, auctionId);
            if(byNameId != null && byNameId.getStatus().equals(EnumUtil.YesNO.NO.toString())){
                pay = true;
            }
        } catch (ValidationException e) {
            model.addAttribute("errorCode",e.getErrorCode());
            return error(e.getMessage(), response, model);
        }
        model.addAttribute("pay",pay);
        return success("成功!!", response, model);
    }

    //交付保证金
    @RequestMapping(value = "/payAuctionMoney", method = RequestMethod.POST)
    public String payAuctionMoney(HttpServletRequest request, HttpServletResponse response, Model model) {
        String auctionId = request.getParameter("id");
        String userName = UserInfoUtils.getUser().getUserName();
        try {
            auctionGuaranteeService.payAuctionMoney(userName,auctionId);
        } catch (ValidationException e) {
            model.addAttribute("errorCode",e.getErrorCode());
            return error(e.getMessage(), response, model);
        }
        return success("成功!!", response, model);
    }
}
