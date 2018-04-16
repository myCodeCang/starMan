package com.thinkgem.jeesite.website.linlianPay;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.*;
import com.thinkgem.jeesite.common.web.BaseController;

import com.thinkgem.jeesite.modules.lianlianPay.constant.PartnerConfig;
import com.thinkgem.jeesite.modules.lianlianPay.constant.ServerURLConfig;
import com.thinkgem.jeesite.modules.lianlianPay.entity.OrderInfo;
import com.thinkgem.jeesite.modules.lianlianPay.entity.OrderParamBean;
import com.thinkgem.jeesite.modules.lianlianPay.entity.PaymentInfo;
import com.thinkgem.jeesite.modules.lianlianPay.until.*;
import com.thinkgem.jeesite.modules.payment.utils.Verify;
import com.thinkgem.jeesite.modules.sys.utils.UserInfoUtils;
import com.thinkgem.jeesite.modules.user.entity.UserOptions;
import com.thinkgem.jeesite.modules.user.entity.UserUserinfo;
import com.thinkgem.jeesite.modules.user.service.UserOptionsService;
import com.thinkgem.jeesite.modules.user.service.UserUserinfoService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by yankai on 2017/6/7.
 */
@Controller
@RequestMapping(value = "${frontPath}/payment/starpay")
public class StarPayController extends BaseController {
    //请求网关地址
    //public static String GATEWAY_URL = "https://openapi.alipay.com/gateway.do";
    public static String GATEWAY_URL = "https://openapi.alipaydev.com/gateway.do";
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";

    @Resource
    private UserOptionsService userOptionsService;
    @Resource
    private UserUserinfoService userUserinfoService;


    @RequestMapping(value = "lianlianPay")
    public String lianlianPay(HttpServletRequest request, HttpServletResponse response,Model model) {
        String money = request.getParameter("money");
        String SERVER = "https://o2o.lianlianpay.com/offline_api/";
        String TRADERNO = "201710270001075189";
        String MD5_KEY = "201408291000007508";
//	 private static String TRADERNO = "201607111000960095";
//	 private static String MD5_KEY = "201607111000960095";

        String prikeyvalue =
                "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKHzGz5Gy0xVa257cySP+XLJ7/bfdWS6xpx2Nt+ZdeA/7QD/a8lPMgx/oTbFFNRh3qBGYAxMLXgwGAxv8dKtdUsi/tTi1VmyNRlA0Ns9Fib5pP9ncaW1isijCmSL3Jey8XMiu0FvTZk8bLVr+hGSADekroCX/JNwNfInc613jE/LAgMBAAECgYAhK3jF/ZwAC3LQkypXL8Hot+GVT9lsgzDQtQmyLG1PO+igDrCV78mRc8aiQCVvBoihjSh4/FVHy5nxWjBOdUaThKkm87tMyfeQ9VTVtiNtAqCNjCbbPmdGFYAIc4X/m14qre8/PtcWyflhtB4xhCHL6BSbJ7emDmik2g6uo3TsOQJBANIr38Db8vIR6AM5onWsyOF/1Iredqi8DpENGhBIWq5raVxL1YaA3zmrmHPZ7Qe2bCaTGWkdSbLB2fNkWJ4eexcCQQDFQ2l0F6OkOo6A3FZDrjHT6jG7qyFe4edZ1JfANa2cOXu6FAHZiTNc8P1IE4YzS8vz4mSj8DWA+5NCi1KYobFtAkBxE2HjJXNkgfXOJmgd38SEbn4v6Nj2l5dOuznWoNcJGIkrOzG6mAOBx6zSR+GbUK6HYoU9ZKCOKHXW2ZpEGCopAkA/UsqRV91SJPIF77VJeKvKKU3Zvq+xs7b+oT4tl42prtJHA5EscdypIztLXekgol5QZgaEkeG7DAdTv0G+eXlpAkAc9BU2xsw4X3h983Hcz3owkzdbd1VRp3TkPtjWL30EisjZvB2XftUUF9BZdG8398sCdvhsTDFyg6TvEs+kfhc4";

        if (StringUtils2.isBlank(money)){
            return error("充值金额有误!",response,model);
        }
        UserUserinfo byName = userUserinfoService.getByName(UserInfoUtils.getUser().getUserName());
        if (byName == null){
            return error("充值用户不存在!",response,model);
        }

        HttpRequestSimple http = HttpRequestSimple.getInstance();
        TestUtil testUtil = new TestUtil();
        OrderParamBean reqBean = new OrderParamBean();

        reqBean.setNo_order(DateUtil.getCurrentDateTimeStr1()+"@"+byName.getUserName());// DateUtil.getCurrentDateTimeStr1()
        reqBean.setOid_partner(TRADERNO);
        reqBean.setMoney_order(money);
        reqBean.setDt_order(DateUtil.getCurrentDateTimeStr1());
        String notifyUrl = Global.getOption("system_sys", "domain");
        reqBean.setNotify_url(notifyUrl+"/payment/lianlianPay/payNotify");  //回调地址
        reqBean.setInfo_order("充值");
        reqBean.setUser_id(byName.getUserName());
        reqBean.setWallet_user_id(byName.getUserName());
        reqBean.setName_goods("充值");
        reqBean.setPay_type("5");  // 0 微信 5 支付宝
        reqBean.setRisk_item("pass");
        reqBean.setSign_type("RSA");
        String sign = testUtil.addSign(
                JSON.parseObject(JSONObject.toJSONString(reqBean)),
                prikeyvalue, MD5_KEY);
        reqBean.setSign(Encodes.urlEncode(sign));
        String url = SERVER + "createOrder_init.htm";
        String resStr = JSON.toJSONString(reqBean);
        String resStr1 = http.postSendHttp(url, resStr);
        if (StringUtils2.isNotBlank(resStr1)){
            JSONObject jsonObject = JSONObject.parseObject(resStr1);
            model.addAttribute("url", jsonObject.get("dimension_url"));
        }
        return success("成功",response,model);
    }


    /**
     * 根据连连支付风控部门要求的参数进行构造风控参数
     * @return
     */
    private String createRiskItem(UserUserinfo userinfo)
    {
        JSONObject riskItemObj = new JSONObject();
        riskItemObj.put("frms_ware_category","2999");
        riskItemObj.put("user_info_mercht_userno",userinfo.getId());
        String time=DateUtils2.formatDateTime(userinfo.getCreateDate());
        String time1=time.replace("-", "");
        String time2=time1.replace(":", "");
        String time3=time2.replace(" ", "");
        riskItemObj.put("user_info_dt_register", time3.trim());
        riskItemObj.put("user_info_bind_phone",userinfo.getMobile());
        riskItemObj.put("user_info_identify_state","1");
        riskItemObj.put("user_info_identify_type","3");
        riskItemObj.put("user_info_full_name",userinfo.getTrueName());
        riskItemObj.put("user_info_id_no",userinfo.getIdCard());
        return riskItemObj.toString();
    }

    public void quickPayInfo(HttpServletRequest req, OrderInfo order,UserUserinfo userinfo)

    {
        StringBuffer strBuf = new StringBuffer();

        strBuf.append("acct_name=");
        strBuf.append(userinfo.getTrueName());
        strBuf.append("&app_request=3");
        strBuf.append("&bg_color=");
        strBuf.append("797676");
        strBuf.append("&busi_partner=");
        strBuf.append("101001");
        strBuf.append("&dt_order=");
        strBuf.append(order.getDt_order());
        strBuf.append("&id_no=");
        strBuf.append(userinfo.getIdCard());
        strBuf.append("&info_order=");
        strBuf.append(order.getInfo_order());
        strBuf.append("&money_order=");
        strBuf.append(order.getMoney_order());
        strBuf.append("&name_goods=");
        strBuf.append("用户充值");
        strBuf.append("&no_order=");
        strBuf.append(order.getNo_order());
        String notifyUrl = Global.getOption("system_sys", "domain");
        strBuf.append("&notify_url=");
        strBuf.append(notifyUrl+"/payment/lianlianPay/payquickPay");
        strBuf.append("&oid_partner=");
        strBuf.append("201710270001075186");
        strBuf.append("&risk_item=");
        strBuf.append(createRiskItem(userinfo));
        strBuf.append("&sign_type=");
        strBuf.append("RSA");
        strBuf.append("&url_return=");
        strBuf.append(notifyUrl+"/f/payment/starpay/quickPaySuccess");
        strBuf.append("&user_id=");
        strBuf.append(userinfo.getId());
        String sign_src = strBuf.toString();
        if (sign_src.startsWith("&"))
        {
            sign_src = sign_src.substring(1);
        }
        String sign = "";
        sign = RSAUtil.sign(PartnerConfig.TRADER_PRI_KEY, sign_src);
//        sign_src += "&key=" + PartnerConfig.MD5_KEY;
//        logger.error("sign_src----"+sign_src);
//        try {
//            sign = Md5Algorithm.getInstance().md5Digest(
//                    sign_src.getBytes("utf-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        PaymentInfo payInfo = new PaymentInfo();
        payInfo.setApp_request("3");
        payInfo.setBg_color("797676");
        payInfo.setBusi_partner("101001");
        payInfo.setDt_order(order.getDt_order());
        payInfo.setId_no(userinfo.getIdCard());
        payInfo.setInfo_order(order.getInfo_order());
        payInfo.setMoney_order(order.getMoney_order());
        payInfo.setName_goods("用户充值");
        payInfo.setNo_order(order.getNo_order());
        payInfo.setNotify_url(notifyUrl+"/payment/lianlianPay/payquickPay");
        payInfo.setOid_partner("201710270001075186");
        payInfo.setAcct_name(userinfo.getTrueName());
        payInfo.setRisk_item(createRiskItem(userinfo));
        payInfo.setSign_type("RSA");
        payInfo.setUrl_return(notifyUrl+"/f/payment/starpay/quickPaySuccess");
        payInfo.setUser_id(userinfo.getId());
        payInfo.setSign(sign);
        String req_data = JSON.toJSONString(payInfo);
        logger.error("req_data----"+req_data);
        req.setAttribute("req_data",req_data);
    }

    @RequestMapping(value = "quickPay")
    public String quickPay(HttpServletRequest req, HttpServletResponse resp,Model model)
            throws ServletException, IOException
    {
        String money = req.getParameter("money");
        if (StringUtils2.isBlank(money)){
            return error("充值金额有误!",resp,model);
        }
        UserUserinfo byName = userUserinfoService.getByName(UserInfoUtils.getUser().getUserName());
        if (byName == null){
            return error("充值用户不存在!",resp,model);
        }
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        // 创建订单
        OrderInfo order = createOrder(req,byName.getUserName());

        quickPayInfo(req, order,byName);

        return "website/themes/lianlianpay/gotoPlainPay";
    }
    @RequestMapping(value = "lianlianPayCheck")
    public String lianlianPayCheck(HttpServletRequest request, HttpServletResponse response, Model model) {

        UserUserinfo byName = userUserinfoService.getByName(UserInfoUtils.getUser().getUserName());
        if (byName == null){
            return error(" 网络繁忙，请稍后重试!", response, model);
        }
        if(StringUtils2.isBlank(byName.getIdCard())){
            return error("请先绑定真实的身份证信息！", response, model);
        }
        return success("成功!!", response, model);
    }
    /**
     * 模拟商户创建订单
     * @param req
     * @return
     */
    private OrderInfo createOrder(HttpServletRequest req,String byName)
    {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setNo_order(LLPayUtil.getCurrentDateTimeStr()+"_"+byName);
        orderInfo.setDt_order(LLPayUtil.getCurrentDateTimeStr());
        orderInfo.setMoney_order(req.getParameter("money"));
        orderInfo.setName_goods("账户充值");
        orderInfo.setInfo_order( byName+ "-充值金额:"+req.getParameter("money"));
        return orderInfo;
    }
    @RequestMapping(value = "quickPaySuccess")
    public String quickPaySuccess(HttpServletRequest req, HttpServletResponse resp,Model model) {
        String notifyUrl = Global.getOption("system_sys", "domain");
        model.addAttribute("goIndex", notifyUrl+"/wap/html/my.html?t=1522051074000");
        return "website/themes/lianlianpay/successpay";
    }
}
