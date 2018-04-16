/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.website.webapp;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.md.service.MdTradeMainService;
import com.thinkgem.jeesite.modules.star.entity.BaseGoodsGroup;
import com.thinkgem.jeesite.modules.star.entity.StarProject;
import com.thinkgem.jeesite.modules.star.service.BaseGoodsGroupService;
import com.thinkgem.jeesite.modules.star.service.SaleByAuctionService;
import com.thinkgem.jeesite.modules.star.service.StarProjectService;
import com.thinkgem.jeesite.modules.sys.utils.UserInfoUtils;
import com.thinkgem.jeesite.modules.trans.entity.TransBuy;
import com.thinkgem.jeesite.modules.trans.entity.TransBuyLog;
import com.thinkgem.jeesite.modules.trans.entity.TransGoods;
import com.thinkgem.jeesite.modules.trans.entity.TransOrder;
import com.thinkgem.jeesite.modules.trans.service.TransBuyLogService;
import com.thinkgem.jeesite.modules.trans.service.TransBuyService;
import com.thinkgem.jeesite.modules.trans.service.TransGoodsService;
import com.thinkgem.jeesite.modules.trans.service.TransOrderService;
import com.thinkgem.jeesite.modules.user.entity.UserUserinfo;
import com.thinkgem.jeesite.modules.user.service.UserUserinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * 转让Controller
 * @author luo
 * @version 2017-09-25
 */
@Controller
@RequestMapping(value = "${frontPath}/star/attorn")
public class AppAttornStarController extends BaseController {
    @Autowired
    private BaseGoodsGroupService baseGoodsGroupService;
    @Autowired
    private TransBuyLogService transBuyLogService ;
    @Autowired
    private TransBuyService transBuyService ;
    @Autowired
    private TransGoodsService transGoodsService ;
    @Autowired
    private TransOrderService transOrderService ;
    @Autowired
    private SaleByAuctionService saleByAuctionService;
    @Autowired
    private MdTradeMainService mdTradeMainService;
    @Autowired
    private StarProjectService starProjectService ;
    @Autowired
    private UserUserinfoService userUserinfoService;
    /**
     * 时间兑换积分
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "exchangeScore")
    public String exchange(HttpServletRequest request, HttpServletResponse response, Model model) {

        String userName = UserInfoUtils.getUser().getUserName();
        String exchangeNum = request.getParameter("exchangeNum");
        String goodsId = request.getParameter("goodsId");
        String groupId = request.getParameter("groupId");

        int realNum;
        int num;
        try {
            num = Integer.parseInt(exchangeNum);
            realNum = transBuyService.exchangeScore(goodsId,groupId, userName, num,"用户");
        } catch (ValidationException e) {
            return error(e.getMessage(),response,model);
        } catch (Exception e){
            return error("兑换失败",response,model);
        }
        if(num == realNum){
            return success ("时间兑换积分成功!!!", response, model);
        }else{
            return success ("时间不足，只兑换了 "+realNum+"秒", response, model);
        }

    }

    /**
     * 获得产品详情
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "getProduct")
    public String getProduct(HttpServletRequest request, HttpServletResponse response, Model model){
        String id = request.getParameter("id");
        StarProject starProduct = starProjectService.get(id);
        model.addAttribute("product",starProduct);
        return success("成功",response,model);
    }

    /**
     * 获得当前明星所有产品
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "getStarPro")
    public String getStarPro(HttpServletRequest request, HttpServletResponse response, Model model){
        String groupId = request.getParameter("groupId");
        StarProject starProject = new StarProject();
        starProject.setStatus(EnumUtil.TransGoodsGroupStatus.up.toString());
        starProject.setGroupId(groupId);
        model.addAttribute("pages",starProjectService.findPage(new Page<StarProject>(request,response),starProject));
        return success("成功",response,model);
    }

    /**
     * 兑换明星产品
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "exchangeProject")
    public String exchangeProject(HttpServletRequest request, HttpServletResponse response, Model model) {
        String id = request.getParameter("id");
        String goodsId = request.getParameter("goodsId");
        String groupId = request.getParameter("groupId");
        UserUserinfo byName = userUserinfoService.getByName(UserInfoUtils.getUser().getUserName());
        if(byName == null){
            return error ("登录异常请重新登录", response, model);
        }
        try {
            transBuyService.exchangeProject(goodsId,groupId,id,byName.getUserName());
        } catch (ValidationException e) {
            return error(e.getMessage(),response,model);
        }
        return success ("兑换明星产品成功!!", response, model);
    }

    /**
     * 当前已购买明星秒数查询
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "getStarNum")
    public String getStarNum(HttpServletRequest request, HttpServletResponse response, Model model) {
        String groupId = request.getParameter("groupId");
        UserUserinfo byName = userUserinfoService.getByName(UserInfoUtils.getUser().getUserName());
        if(byName == null){
            return error ("登录异常请重新登录", response, model);
        }
        BigDecimal attornScale;
        BigDecimal chaExchangeNum;
        TransGoods transGoods = transGoodsService.getUserStar(groupId,byName.getUserName());
        try {
            attornScale = saleByAuctionService.getByGroupId(groupId).getMoney().multiply(new BigDecimal(Global.getOption("system_trans","attorn_scale")));
            model.addAttribute("scale",Global.getOption("system_trans","attorn_scale"));
        } catch (Exception e) {
            return error("兑换比例读取错误。",response,model);
        }
        if(transGoods != null){
            chaExchangeNum = BigDecimal.valueOf(transGoods.getNum()).multiply(attornScale);
            model.addAttribute("chaExchangeNum",chaExchangeNum.intValue());
        }
        model.addAttribute("transGoods", transGoods);
        return success ("成功!!", response, model);
    }


    /**
     * 获取明星信息
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "getStarDetail")
    public String getStarDetail(HttpServletRequest request, HttpServletResponse response, Model model) {
        String groupId = request.getParameter("groupId");
        BaseGoodsGroup goodsGroup = baseGoodsGroupService.get(groupId);
        if(goodsGroup == null){
            return error ("明星不存在或已被删除。", response, model);
        }
        model.addAttribute("goodsGroup",goodsGroup);
            return success ("成功!!", response, model);
    }


    /**
     * 兑换明星产品记录查询
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "exchangeStarPro")
    public String exchangeStarPro(HttpServletRequest request, HttpServletResponse response, Model model) {
        UserUserinfo byName = userUserinfoService.getByName(UserInfoUtils.getUser().getUserName());
        if(byName == null){
            return error ("登录异常请重新登录", response, model);
        }
        TransOrder  transOrder = new TransOrder();
        transOrder.setUserName(byName.getUserName());
        model.addAttribute("page",transOrderService.findPage(new Page<TransOrder>(request,response),transOrder));
        return success ("成功!!", response, model);
    }



    /**
     * 查询所有超过交易日的明星
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "getStarOutTime")
    public String getStarOutTime(HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BaseGoodsGroup> goodsGroups;
        BaseGoodsGroup goodsGroup = new BaseGoodsGroup();
        goodsGroup.setAttornFind("1");
        goodsGroups = baseGoodsGroupService.findPage(new Page<BaseGoodsGroup>(request,response),goodsGroup);
        model.addAttribute("page", goodsGroups);
        return success ("成功!!", response, model);
    }

    /**
     * 获得用户时间价值
     * @return
     */
    @RequestMapping(value = "getTimeValue")
    public String getTimeValue(HttpServletRequest request, HttpServletResponse response, Model model){

        UserUserinfo byName = userUserinfoService.getByName(UserInfoUtils.getUser().getUserName());

        if(byName == null){
            return error("登录超时，请重新登录。",response,model);
        }
        TransGoods transGoods = new TransGoods();
        transGoods.setStatus(EnumUtil.GoodsType.hold.toString());
        transGoods.setUserName(byName.getUserName());
        List<TransGoods> transList = transGoodsService.findList(transGoods);
        BigDecimal num = BigDecimal.ZERO;
        for (TransGoods goods : transList) {
            try {
                num = num.add(BigDecimal.valueOf(goods.getNum()).multiply(mdTradeMainService.getNowMoney(goods.getGroupId()).getTransactionPrice()));
            } catch (Exception e) {
                return error("获取最新价失败",response,model);
            }
        }
        model.addAttribute("num",num);
        return success("成功",response,model);
    }

    /**
     * 获取转让记录
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "getAttornLog")
    public String getAttornLog(HttpServletRequest request ,HttpServletResponse response , Model model){
        UserUserinfo byName = userUserinfoService.getByName(UserInfoUtils.getUser().getUserName());
        if(byName == null){
            return error("登录超时，请重新登录。",response,model);
        }
        TransBuyLog transBuyLog = new TransBuyLog();
        transBuyLog.setUserName(byName.getUserName());
        model.addAttribute("pages",transBuyLogService.findPage(new Page<TransBuyLog>(request,response),transBuyLog));
        return success("成功。",response,model);
    }

    /**
     * 查询交易大厅详情
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "getAttornBuy")
    public String getAttornBuy (HttpServletRequest request, HttpServletResponse response, Model model){
        TransBuy transBuy = new TransBuy();
        String groupId = request.getParameter("groupId");
        UserUserinfo byName = userUserinfoService.getByName(UserInfoUtils.getUser().getUserName());
        if(byName == null){
            return error("登录超时，请重新登录。",response,model);
        }
        transBuy.setGroupId(groupId);
        transBuy.setSellUserName(byName.getUserName());
        transBuy.setStatus(EnumUtil.GoodsType.Selling.toString());
        model.addAttribute("pages",transBuyService.findPage(new Page<TransBuy>(request,response),transBuy));
        return success("成功！",response,model);
    }


    /**
     * 下架时间
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "promiseCancel")
    public String promiseCancel(HttpServletRequest request, HttpServletResponse response, Model model){
        String buyId = request.getParameter("buyId");
        String userName = UserInfoUtils.getUser().getUserName();
        transBuyService.promiseCancel(userName,buyId,EnumUtil.TransBuyStatus.downUser,"用户下架");
        return success("下架成功！",response,model);
    }

    /**
     * 查询当前用户所持有的所有明星的时间
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "getUserProducts")
    public String getUserProducts(HttpServletRequest request, HttpServletResponse response, Model model){
        UserUserinfo byName = userUserinfoService.getByName(UserInfoUtils.getUser().getUserName());
        if(byName == null){
            return error("登录超时，请重新登录。",response,model);
        }
        TransGoods transGoods = new TransGoods();
        transGoods.setStatus(EnumUtil.GoodsType.hold.toString());
        transGoods.setUserName(byName.getUserName());
        Page<TransGoods> page = transGoodsService.findPage(new Page<TransGoods>(request,response),transGoods);
        List<TransGoods> list = page.getList();
        int count = 0;
        for (int i = 0;i<list.size();i++) {
            if(list.get(i).getNum() != 0){
                count++;
            }
            try {
                list.get(i).setBuyMoney(mdTradeMainService.getNowMoney(list.get(i).getGroupId()).getTransactionPrice());
            } catch (Exception e) {
                return error("获取最新价失败",response,model);
            }
        }
        page.setCount(count);
        model.addAttribute("pages",page);
        return success("成功。",response,model);
    }

}