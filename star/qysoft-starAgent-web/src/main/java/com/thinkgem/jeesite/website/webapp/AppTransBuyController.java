/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.website.webapp;

import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.utils.UserInfoUtils;
import com.thinkgem.jeesite.modules.trans.service.TransBuyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;


/**
 * 用户交易/转让
 *
 * @author ThinkGem
 * @version 2013-5-29
 */
@Controller
@RequestMapping(value = "${frontPath}/app/transBuy")
public class AppTransBuyController extends BaseController {

    @Resource
    private TransBuyService transBuyService;


	/**
	 * 根据group id , 查询交易表买卖数据
	 * 表名: trans_buy
	 * 条件: group_id
	 * 其他: 1.需要根据价格分组查询. 查询发布的买画价格最高的5条 和 卖花价格最低的5条
	 */
	@RequestMapping(value = "/transBuyList", method = RequestMethod.POST)
	public String transBuyList(HttpServletRequest request, HttpServletResponse response, Model model) {
		String groupId = request.getParameter("groupId");
		model.addAttribute("buyTransList",transBuyService.getBuyGroupListLimit(groupId,5));
		model.addAttribute("sellTransList",transBuyService.getSellGroupListLimit(groupId,5));

        return success("成功!!", response, model);
    }


    /**
     * 发布应卖单
     * 表名: trans_buy  ,userinfo, trans_goods
     * 条件: 发布应卖单
     * 其他:
     */
    @RequestMapping(value = "/publishSell", method = RequestMethod.POST)
    public String publishSell(HttpServletRequest request, HttpServletResponse response, Model model) throws ValidationException {

        try {

            int num = Integer.parseInt(request.getParameter("attornNum"));
            String money = request.getParameter("attornMoney");
            String groupId = request.getParameter("groupId");
            String userName = UserInfoUtils.getUser().getUserName();

            transBuyService.publishSell(userName, groupId, new BigDecimal(money), num);
            return success("发布成功!!", response, model);

        } catch (ValidationException e) {
            return error(e.getMessage(), response, model);
        } catch (NumberFormatException e) {
            return error("转让数量输入有误!", response, model);
        } catch (Exception e){
            return error("发布失败",response,model);
        }

    }


    /**
     * 发布应买单
     * 表名: trans_buy , userinfo , trans_goods
     * 条件: 发布应卖单
     * 其他:
     */
    @RequestMapping(value = "/publishBuy", method = RequestMethod.POST)
    public String publishBuy(HttpServletRequest request, HttpServletResponse response, Model model) throws ValidationException {
        try {

            int num = Integer.parseInt(request.getParameter("attornNum"));
            String money = request.getParameter("attornMoney");//单价
            String groupId = request.getParameter("groupId");
            String userName = UserInfoUtils.getUser().getUserName();

            transBuyService.publishBuy(userName, groupId, new BigDecimal(money), num);
            return success("发布成功!!", response, model);

        } catch (ValidationException e) {
            return error(e.getMessage(), response, model);
        } catch (Exception e) {
            return error("失败!", response, model);
        }

    }


    /**
     * 响应应卖单== 买
     * 表名: trans_buy , userinfo , trans_order , trans_goods , trans_buy_log
     * 条件: 根据应卖价格和数量, 买入相应艺术品
     * 其他:
     */
    @RequestMapping(value = "/promiseSell", method = RequestMethod.POST)
    public String promiseSell(HttpServletRequest request, HttpServletResponse response, Model model) throws ValidationException {

        try {

            String groupId = request.getParameter("groupId");
            BigDecimal goodsMoney = new BigDecimal(request.getParameter("money"));
            BigDecimal num = new BigDecimal(request.getParameter("number"));
            String userName = UserInfoUtils.getUser().getUserName();

            int buyNum = transBuyService.promiseSell(userName, groupId, goodsMoney, num);
            if (buyNum <= 0) {
                return error("来晚一步,商品已被购买一空!", response, model);
            } else if (buyNum < num.intValue()) {
                return success("手慢一步,仅成功购买到" + buyNum + "件商品.", response, model);
            } else {
                return success("恭喜您,成功购买到" + buyNum + "件商品.", response, model);
            }


        } catch (ValidationException e) {
            return error(e.getMessage(), response, model);
        }catch (Exception e2){
            return error("参数错误",response, model);
        }


    }


    /**
     * 响应应买单 == 卖
     * 表名: trans_buy , userinfo , trans_order , trans_goods  , trans_buy_log
     * 条件: 根据应买价格和数量, 卖出相应艺术品
     * 其他:
     */
    @RequestMapping(value = "/promiseBuy", method = RequestMethod.POST)
    public String promiseBuy(HttpServletRequest request, HttpServletResponse response, Model model) throws ValidationException {

        try {

            String groupId = request.getParameter("groupId");
            BigDecimal goodsMoney = new BigDecimal(request.getParameter("money"));
            BigDecimal num = new BigDecimal(request.getParameter("num"));
            String userName = UserInfoUtils.getUser().getUserName();

            int buyNum = transBuyService.promiseBuy(userName, groupId, goodsMoney, num);
            if (buyNum <= 0) {
                return error("来晚一步,本需求已经全部求购完毕!", response, model);
            } else if (buyNum < num.intValue()) {
                return success("手慢一步,仅成功卖出" + buyNum + "件商品.", response, model);
            } else {
                return success("恭喜您,成功卖出" + buyNum + "件商品.", response, model);
            }

        } catch (Exception e) {
            return error(e.getMessage(), response, model);
        }
    }

}
