/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.website.webapp;

import com.alibaba.druid.support.json.JSONUtils;
import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils2;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.md.entity.MdTrade;
import com.thinkgem.jeesite.modules.md.entity.MdTradeLog;
import com.thinkgem.jeesite.modules.md.entity.MdTradeMain;
import com.thinkgem.jeesite.modules.md.entity.TradeType;
import com.thinkgem.jeesite.modules.md.service.MdTradeLogService;
import com.thinkgem.jeesite.modules.md.service.MdTradeMainService;
import com.thinkgem.jeesite.modules.md.service.MdTradeService;
import com.thinkgem.jeesite.modules.star.dao.RealTimePriceDao;
import com.thinkgem.jeesite.modules.star.entity.BaseGoodsGroup;
import com.thinkgem.jeesite.modules.star.entity.RealTimePrice;
import com.thinkgem.jeesite.modules.star.entity.ShopClassify;
import com.thinkgem.jeesite.modules.star.entity.UserColection;
import com.thinkgem.jeesite.modules.star.service.BaseGoodsGroupService;
import com.thinkgem.jeesite.modules.star.service.ShopClassifyService;
import com.thinkgem.jeesite.modules.star.service.UserColectionService;
import com.thinkgem.jeesite.modules.sys.utils.UserInfoUtils;
import com.thinkgem.jeesite.modules.trans.entity.KChartData;
import com.thinkgem.jeesite.modules.trans.entity.TransGoods;
import com.thinkgem.jeesite.modules.trans.service.TransGoodsService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 撮合交易
 *
 * @author ThinkGem
 * @version 2013-5-29
 */
@Controller
@RequestMapping(value = "${frontPath}/app/married")
public class AppMarriedController extends BaseController {
    @Resource
    private ShopClassifyService shopClassifyService;
    @Resource
    private BaseGoodsGroupService baseGoodsGroupService;
    @Resource
    private UserColectionService userColectionService;
    @Resource
    private TransGoodsService transGoodsService;
    @Resource
    private MdTradeLogService mdTradeLogService;
    @Resource
    private MdTradeService mdTradeService;
    @Resource
    private MdTradeMainService mdTradeMainService;
    @Resource
    private RealTimePriceDao realTimePriceDao;

    /**
     *获取撮合交易价格波动配置
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/getMdConfigInfo", method = RequestMethod.POST)
    public String getMdConfigInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
        String groupId = request.getParameter("groupId");
        BigDecimal profitPercent = new BigDecimal(Global.getOption("md_config","buy_procedure_money"));
        Map<String, Object> priceMap = mdTradeService.vailIsRationalPrice(groupId);
        model.addAttribute("hightestPrice",priceMap.get("hightestPrice"));
        model.addAttribute("lowestPrice",priceMap.get("lowestPrice"));
        model.addAttribute("profit",profitPercent);
        return success("成功!!", response, model);
    }

    /**
     * 查询明星分类列表
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/getStarCategory", method = RequestMethod.POST)
    public String getStarCategory(HttpServletRequest request, HttpServletResponse response, Model model) {
        List<ShopClassify> categoryList = shopClassifyService.findList(new ShopClassify());
        model.addAttribute("categoryList",categoryList);
        return success("成功!!", response, model);
    }

    /**
     * 根据id获取该明星基本信息
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/getStarBaseInfo", method = RequestMethod.POST)
    public String getStarBaseInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
        String groupId = request.getParameter("id");
        BaseGoodsGroup baseGoodsGroup = baseGoodsGroupService.get(groupId);
        if(baseGoodsGroup==null){
            return error("该明星不存在!", response, model);
        }
        model.addAttribute("baseInfo",baseGoodsGroup);
        return success("成功!!", response, model);
    }


    /**
     * 获取首页明星列表
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/getTransingList", method = RequestMethod.POST)
    public String getTransingList(HttpServletRequest request, HttpServletResponse response, Model model) {
        BaseGoodsGroup baseGoodsGroup = new BaseGoodsGroup();
        baseGoodsGroup.setIsShow(EnumUtil.YesNO.YES.toString());
        baseGoodsGroup.setStatus(EnumUtil.YesNO.YES.toString());
        MdTradeMain mdTradeMain = new MdTradeMain();
        mdTradeMain.setCreateDate(new Date());
        baseGoodsGroup.setMdTradeMain(mdTradeMain);
        List<BaseGoodsGroup> baseGoodsList = baseGoodsGroupService.findTradeList(baseGoodsGroup);

        model.addAttribute("baseGoodsList",baseGoodsList);
        return success("成功!!", response, model);
    }



    /**
     * 根据产品id获取该产品的信息
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/getGoodsInfoByGroupId", method = RequestMethod.POST)
    public String getGoodsInfoByGroupId(HttpServletRequest request, HttpServletResponse response, Model model) {
        String groupId = request.getParameter("groupId");
        BaseGoodsGroup baseGoodsGroup = new BaseGoodsGroup();
        baseGoodsGroup.setId(groupId);
        MdTradeMain mdTradeMain = new MdTradeMain();
        mdTradeMain.setCreateDate(new Date());
        baseGoodsGroup.setMdTradeMain(mdTradeMain);
        List<BaseGoodsGroup> baseGoodsInfoList = baseGoodsGroupService.findTradeList(baseGoodsGroup);

        Map<String, Object> priceMap = mdTradeService.vailIsRationalPrice(groupId);
        Date beginTime = DateUtils2.formatStrTime(Global.getOption("md_config", "md_time_begin"));
        Date beginTime2 = DateUtils2.formatStrTime(Global.getOption("md_config", "md_time_begin2"));
        Date beginTime3 = DateUtils2.formatStrTime(Global.getOption("md_config", "md_time_begin3"));
        Date endTime = DateUtils2.formatStrTime(Global.getOption("md_config", "md_time_end"));
        Date endTime2 = DateUtils2.formatStrTime(Global.getOption("md_config", "md_time_end2"));
        Date endTime3 = DateUtils2.formatStrTime(Global.getOption("md_config", "md_time_end3"));
        double seconds = DateUtils2.getSecondsOfTwoDate(beginTime,endTime);
        double seconds2 = DateUtils2.getSecondsOfTwoDate(beginTime2,endTime2);
        double seconds3 = DateUtils2.getSecondsOfTwoDate(beginTime3,endTime3);
        //获取卖1的价格
        String type = TradeType.SELL_OUT.toString();
        MdTrade sellOnePrice = mdTradeService.getSellOnePrice(groupId, type);
        boolean havePrice = true;
        if (sellOnePrice !=null){
            model.addAttribute("sellOnePrice",sellOnePrice.getPrice());
            havePrice = false;
        }
        model.addAttribute("state",havePrice);
        model.addAttribute("lowest",priceMap.get("lowestPrice"));
        model.addAttribute("hightest",priceMap.get("hightestPrice"));
        model.addAttribute("baseGoodsInfo",baseGoodsInfoList);
        model.addAttribute("beginTime",DateUtils2.getApppointTime(beginTime));
        model.addAttribute("beginTime2",DateUtils2.getApppointTime(beginTime2));
        model.addAttribute("beginTime3",DateUtils2.getApppointTime(beginTime3));
        model.addAttribute("endTime",DateUtils2.getApppointTime(endTime));
        model.addAttribute("endTime2",DateUtils2.getApppointTime(endTime2));
        model.addAttribute("endTime3",DateUtils2.getApppointTime(endTime3));
        model.addAttribute("seconds",seconds);
        model.addAttribute("seconds2",seconds2);
        model.addAttribute("seconds3",seconds3);
        return success("成功!!", response, model);
    }

    /**
     * 撮合交易求购
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/marriedBuy", method = RequestMethod.POST)
    public String marriedBuy(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            int num = Integer.parseInt(request.getParameter("number"));
            String money = request.getParameter("money");//单价
            String groupId = request.getParameter("groupId");
            String userName = UserInfoUtils.getUser().getUserName();
            if(StringUtils2.isBlank(userName)){
                return error("登录超时,请重新登录!",response,model);
            }
            String payMethod = request.getParameter("payMethod");
            mdTradeService.publishBuy(userName, groupId, new BigDecimal(money), num,payMethod);
            return success("发布成功!!", response, model);
        } catch (ValidationException e) {
            model.addAttribute("errorCode",e.getErrorCode());
            return error(e.getMessage(), response, model);
        }catch (Exception e) {
            return error("失败,请稍后再试!", response, model);
        }
    }

    /**
     * 撮合交易转让
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/marriedSell", method = RequestMethod.POST)
    public String marriedSell(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            int num = Integer.parseInt(request.getParameter("number"));
            String money = request.getParameter("money");//单价
            String groupId = request.getParameter("groupId");
            String userName = UserInfoUtils.getUser().getUserName();
            String payMethod = request.getParameter("payMethod");
            mdTradeService.publishSell(userName, groupId, new BigDecimal(money), num,payMethod);
            return success("发布成功!!", response, model);
        } catch (ValidationException e) {
            model.addAttribute("errorCode",e.getErrorCode());
            return error(e.getMessage(), response, model);
        } catch (Exception e) {
            return error("失败!", response, model);
        }
    }

    /**
     *获取用户当前持有量
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/getUserHold", method = RequestMethod.POST)
    public String getUserHold(HttpServletRequest request, HttpServletResponse response, Model model) {
        String groupId = request.getParameter("groupId");
        String userName = UserInfoUtils.getUser().getUserName();
        if(StringUtils2.isBlank(userName)){
            return error("登录超时,请重新登录!",response,model);
        }
        TransGoods transGoods = transGoodsService.getUserStar(groupId,userName);
        model.addAttribute("transGoods",transGoods);
        return success("成功!!", response, model);
    }


    /**
     *根据分类id筛选明星
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    public String filter(HttpServletRequest request, HttpServletResponse response, Model model) {
        String categoryId = request.getParameter("categoryId");
        BaseGoodsGroup baseGoodsGroup = new BaseGoodsGroup();
        baseGoodsGroup.setStatus(EnumUtil.YesNO.YES.toString());
        baseGoodsGroup.setCategoryId(categoryId);
        MdTradeMain mdTradeMain = new MdTradeMain();
        mdTradeMain.setCreateDate(new Date());
        baseGoodsGroup.setMdTradeMain(mdTradeMain);
        List<BaseGoodsGroup> starlist = baseGoodsGroupService.findTradeList(baseGoodsGroup);

        model.addAttribute("starlist",starlist);
        return success("成功!!", response, model);
    }

    /**
     *收藏明星
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/colectStar", method = RequestMethod.POST)
    public String colectStar(HttpServletRequest request, HttpServletResponse response, Model model) {
        String groupId = request.getParameter("groupId");
        BaseGoodsGroup baseGoodsGroup = baseGoodsGroupService.get(groupId);
        if (baseGoodsGroup==null){
            return error("您所收藏的明星不存在!",response,model);
        }
        String loginName = UserInfoUtils.getUser().getUserName();
        if(StringUtils2.isBlank(loginName)){
            return error("登录超时,请重新登录!",response,model);
        }
        UserColection byNameAndId = userColectionService.getByNameAndId(loginName, groupId);
        if(byNameAndId != null){
            return error("您已收藏过该明星,请勿重复收藏!",response,model);
        }
        UserColection userColection = new UserColection();
        userColection.setGroupId(groupId);
        userColection.setUserName(UserInfoUtils.getUser().getUserName());
        userColectionService.save(userColection);
        return success("成功!!", response, model);
    }

    /**
     *取消收藏
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/cancelColect", method = RequestMethod.POST)
    public String cancelColect(HttpServletRequest request, HttpServletResponse response, Model model) {
        String groupId = request.getParameter("groupId");
        String userName = UserInfoUtils.getUser().getUserName();
        if(StringUtils2.isBlank(userName)){
            return error("登录超时,请重新登陆!",response,model);
        }
        userColectionService.cancelColection(userName,groupId);
        return success("成功!!", response, model);
    }

    /**
     *获取收藏明星列表
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/getColectStar", method = RequestMethod.POST)
    public String getColectStar(HttpServletRequest request, HttpServletResponse response, Model model) {
        String LoginName = UserInfoUtils.getUser().getUserName();
        if(StringUtils2.isBlank(LoginName)){
            return error("登录超时,请重新登录!",response,model);
        }
        BaseGoodsGroup baseGoodsGroup = new BaseGoodsGroup();
        baseGoodsGroup.setStatus("1");
        MdTradeMain mdTradeMain = new MdTradeMain();
        mdTradeMain.setCreateDate(new Date());
        baseGoodsGroup.setMdTradeMain(mdTradeMain);
        List<BaseGoodsGroup> baseGoodsGroupList = baseGoodsGroupService.findTradeList(baseGoodsGroup);

        List<UserColection> colectionList = userColectionService.findByUserName(LoginName);
        Set<String> collect1 = colectionList.stream().map(UserColection::getGroupId).collect(Collectors.toSet());
        List<BaseGoodsGroup> collect = baseGoodsGroupList.stream().filter(p -> collect1.contains(p.getId())).collect(Collectors.toList());
        model.addAttribute("colectionList",collect);
        return success("成功!!", response, model);
    }

    /**
     *查询持有的明星产品
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/getHoldGoods", method = RequestMethod.POST)
    public String getHoldGoods(HttpServletRequest request, HttpServletResponse response, Model model) {
        String userName = UserInfoUtils.getUser().getUserName();
        if(StringUtils2.isBlank(userName)){
            return error("登录超时,请重新登录!",response,model);
        }
        TransGoods transGoods = new TransGoods();
        transGoods.setStatus(EnumUtil.GoodsType.hold.toString());
        transGoods.setUserName(userName);
        List<TransGoods> holdGoodsList = transGoodsService.findList(transGoods);
        for(TransGoods goods:holdGoodsList){
            MdTradeMain mdTradeMain = mdTradeMainService.getNowMoney(goods.getGroupId());
            if(mdTradeMain == null){
                return error("获取最新价格失败!!", response, model);
            }
            goods.setTransactionPrice(mdTradeMain.getTransactionPrice());

        }
        model.addAttribute("holdGoodsList",holdGoodsList);
        return success("成功!!", response, model);
    }

    /**
     *查询当日交易记录
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/getTodayTransLog", method = RequestMethod.POST)
    public String getTodayTransLog(HttpServletRequest request, HttpServletResponse response, Model model) {
        String userName = UserInfoUtils.getUser().getUserName();
        if (StringUtils2.isBlank(userName)){
            return error("登录超时,请重新登录!",response,model);
        }
        MdTradeLog mdTradeLog = new MdTradeLog();
        mdTradeLog.setUserName(userName);
        mdTradeLog.setCreateDate(new Date());
        model.addAttribute("todayTransList",mdTradeLogService.findPage(new Page<MdTradeLog>(request, response), mdTradeLog));
        return success("成功!!", response, model);
    }

    /**
     *查询历史交易记录
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/getHistoryTransLog", method = RequestMethod.POST)
    public String getHistoryTransLog(HttpServletRequest request, HttpServletResponse response, Model model) {
        String userName = UserInfoUtils.getUser().getUserName();
        String beginTime = request.getParameter("beginTime");
        String endTime = request.getParameter("endTime");
        if (StringUtils2.isBlank(userName)){
            return error("登录超时,请重新登录!",response,model);
        }
        MdTradeLog mdTradeLog = new MdTradeLog();
        mdTradeLog.setUserName(userName);
        mdTradeLog.setCreateDateBegin(beginTime);
        mdTradeLog.setCreateDateEnd(endTime);
        model.addAttribute("historyTransList",mdTradeLogService.findPage(new Page<MdTradeLog>(request, response), mdTradeLog));
        return success("成功!!", response, model);
    }

    /**
     *查询分时图所需数据
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/getEchartsRealTimeData", method = RequestMethod.POST)
    public String getEchartsRealTimeData(HttpServletRequest request, HttpServletResponse response, Model model) {
        String groupId = request.getParameter("groupId");
        RealTimePrice realTimePrice = new RealTimePrice();
        realTimePrice.setGroupId(groupId);
        realTimePrice.setCreateDate(new Date());
        List<RealTimePrice> list = realTimePriceDao.findList(realTimePrice);
        List<RealTimePrice> timeShareData = list.stream().sorted(Comparator.comparing(RealTimePrice::getCreateDate)).collect(Collectors.toList());
        model.addAttribute("timeShareData",timeShareData);
        return success("成功!!", response, model);
    }

    /**
     *查询交易表
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/getTransOrder", method = RequestMethod.POST)
    public String getTransOrder(HttpServletRequest request, HttpServletResponse response, Model model) {
        String userName = UserInfoUtils.getUser().getUserName();
        if (StringUtils2.isBlank(userName)){
            return error("登录超时,请重新登录!",response,model);
        }
        MdTrade mdTrade = new MdTrade();
        mdTrade.setUserName(userName);
        model.addAttribute("orderList",mdTradeService.findPage(new Page<MdTrade>(request, response), mdTrade));
        return success("成功!!", response, model);
    }

    /**查询五档、
     * 根据group id , 查询交易表买卖数据
     * 表名: md_trade
     * 条件: group_id
     * 其他: 1.需要根据价格分组查询. 查询发布的发布价格最高的5条 和 交易价格最低的5条
     */
    @RequestMapping(value = "/mdTradeList", method = RequestMethod.POST)
    public String mdTradeList(HttpServletRequest request, HttpServletResponse response, Model model) {
        String groupId = request.getParameter("groupId");
//        model.addAttribute("buyTransList",mdTradeService.getBuyGroupListLimit(groupId,5));
//        model.addAttribute("sellTransList",mdTradeService.getSellGroupListLimit(groupId,5));
        String fiveSpeedInfo ;
        File path = new File(this.getClass().getResource("/").getPath());
        StringBuffer pa = new StringBuffer();
        pa.append(path).append(File.separator).append("order").append(File.separator).append(groupId).append(".txt");
        try {
            fiveSpeedInfo = FileUtils.readFileToString(new File(pa.toString()));

        } catch (IOException e) {
            return error("获取五档信息失败"+e.getMessage(),response,model);
        }
        model.addAttribute("fiveSpeedInfo",fiveSpeedInfo);
        return success("成功",response,model);
    }

    /**查询行情详情
     * 表名: md_trade_mian
     * 条件: group_id
     * 其他:
     */
    @RequestMapping(value = "/marketDetail", method = RequestMethod.POST)
    public String marketDetail(HttpServletRequest request, HttpServletResponse response, Model model) {
        String groupId = request.getParameter("groupId");
        String dealInfo ;
        File path = new File(this.getClass().getResource("/").getPath());
        StringBuffer pa = new StringBuffer();
        pa.append(path).append(File.separator).append("dealLog").append(File.separator).append(groupId).append(".txt");
        try {
            dealInfo = FileUtils.readFileToString(new File(pa.toString()));
        } catch (IOException e) {
            return error("获取成交信息失败"+e.getMessage(),response,model);
        }
        model.addAttribute("logList",dealInfo);
        return success("成功",response,model);
    }

    /**
     *执行撤单操作
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/killOrder", method = RequestMethod.POST)
    public String killOrder(HttpServletRequest request, HttpServletResponse response, Model model) {
        String orderId = request.getParameter("orderId");
        String userName = UserInfoUtils.getUser().getUserName();
        if (StringUtils2.isBlank(userName)){
            return error("登录超时,请重新登录!",response,model);
        }
        try {
            mdTradeService.cancelOrder(orderId,userName);
        } catch (ValidationException e) {
            return error(e.getMessage(),response,model);
        }
        return success("成功!!", response, model);
    }

    /**
     * 分时图读取json文件
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "getNowPrice")
    public String getNowPrice(HttpServletRequest request, HttpServletResponse response, Model model){
        String nowPrice ;
        File path = new File(this.getClass().getResource("/").getPath());
        String groupId = request.getParameter("groupId");
        StringBuffer pa = new StringBuffer();
        pa.append(path).append(File.separator).append("nowprice").append(File.separator).append(groupId).append(".txt");
        try {
            nowPrice = FileUtils.readFileToString(new File(pa.toString()));
        } catch (IOException e) {
            return error("获取最新价失败"+e.getMessage(),response,model);
        }
        model.addAttribute("json",nowPrice);
        return success("成功",response,model);
    }

    /**
     *查询柱形图所需数据
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/getEchartsData", method = RequestMethod.POST)
    public String getEchartsData(HttpServletRequest request, HttpServletResponse response, Model model) {
        String groupId = request.getParameter("groupId");
        MdTradeLog mdTradeLog = new MdTradeLog();
        mdTradeLog.setGroupId(groupId);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("MM-dd");
        String startDate = format.format(DateUtils2.getDateBefore(new Date(),5));
        String endDate = DateUtils2.getDate();
        List<MdTradeLog> list = mdTradeLogService.findEchartsList(groupId,startDate,endDate);
        List<Integer> amount = new ArrayList<Integer>();
        List<String> date = new ArrayList<String>();
        for (MdTradeLog echartList:list){
            amount.add(echartList.getAmountSum()/2);
            date.add(format2.format(echartList.getCreateDate()));
        }
        model.addAttribute("amount",amount);
        model.addAttribute("date",date);
        return success("成功!!", response, model);
    }

    /**
     *查询K线图所需数据
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/getKlineData", method = RequestMethod.POST)
    public String getKlineData(HttpServletRequest request, HttpServletResponse response, Model model) {
        String groupId = request.getParameter("groupId");
        MdTradeMain mdTradeMain = new MdTradeMain();
        mdTradeMain.setGroupId(groupId);
        List<MdTradeMain> dayKlineDataList = mdTradeMainService.findList(mdTradeMain);
        List<String[]> list = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        for (MdTradeMain dayKline : dayKlineDataList) {
            String date = format.format(dayKline.getCreateDate());
            String[] data = new String[]{date,dayKline.getOpeningPrice().toString(),
            dayKline.getClosingPrice().toString(),dayKline.getLowestPrice().toString(),dayKline.getHighestPrice().toString()};
            list.add(data);
        }
        model.addAttribute("map",list);
        return success("成功!!", response, model);
    }

}
