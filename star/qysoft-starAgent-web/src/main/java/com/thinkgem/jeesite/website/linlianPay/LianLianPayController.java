package com.thinkgem.jeesite.website.linlianPay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.web.BaseController;

import com.thinkgem.jeesite.modules.lianlianPay.constant.PartnerConfig;
import com.thinkgem.jeesite.modules.lianlianPay.entity.PayDataBean;
import com.thinkgem.jeesite.modules.lianlianPay.entity.RetBean;
import com.thinkgem.jeesite.modules.lianlianPay.until.FuncUtils;
import com.thinkgem.jeesite.modules.lianlianPay.until.LLPayUtil;
import com.thinkgem.jeesite.modules.lianlianPay.until.TestUtil;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.user.dao.UserChargeLogDao;
import com.thinkgem.jeesite.modules.user.entity.UserChargeLog;
import com.thinkgem.jeesite.modules.user.entity.UserUserinfo;
import com.thinkgem.jeesite.modules.user.service.UserChargeLogService;
import com.thinkgem.jeesite.modules.user.service.UserExStarService;
import com.thinkgem.jeesite.modules.user.service.UserUserinfoService;
import com.thinkgem.jeesite.modules.wechat.service.WeixinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/7/25.
 */
@Controller
@RequestMapping("/payment/lianlianPay")
public class LianLianPayController extends BaseController {
    private static final String RETURN_SUCCESS = "SUCCESS";
    private static final String RETURN_FAILED = "FAIL";

    @Autowired(required = false)
    private UserUserinfoService userUserinfoService;
    @Autowired
    private UserChargeLogService userChargeLogService;

    @Autowired
    private WeixinService wexinService;

    @Autowired
    private UserExStarService userExStarService;



    @ResponseBody
    @RequestMapping(value = "payNotify")
    public String payNotify(HttpServletRequest request, HttpServletResponse response) {
        String pubRsa="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSS/DiwdCf/aZsxxcacDnooGph3d2JOj5GXWi+\n" +
                "q3gznZauZjkNP8SKl3J2liP0O6rU/Y/29+IUe+GTMhMOFJuZm1htAtKiu5ekW0GlBMWxf4FPkYlQ" +
                "kPE0FtaoMP3gYfh+OwI+fIRrpW3ySn3mScnc6Z700nU/VYrRkfcSCbSnRwIDAQAB";
        String md5Key = "201408291000007508";
        RetBean retBean = new RetBean();
        retBean.setRet_code("9999");
        retBean.setRet_msg("交易失败");
        String resut = getString(request);
        if (StringUtils2.isBlank(resut)){
            return JSON.toJSONString(retBean);
        }else{
            JSONObject jsonObject = JSONObject.parseObject(resut);
            //验签
            boolean checkSign = TestUtil.checkSign(jsonObject, pubRsa, md5Key);
            if (checkSign){
                if ("SUCCESS".equals(jsonObject.get("result_pay"))){
                    boolean addMoney = userAddMoney(jsonObject);
                    if  (!addMoney){
                        return JSON.toJSONString(retBean);
                    }
                }
            }
        }
        retBean.setRet_code("0000");
        retBean.setRet_msg("交易成功");
        return JSON.toJSONString(retBean);
    }

    private String getString(HttpServletRequest request) {
        ByteArrayOutputStream ms = null;
        String respText = request.getQueryString();
        if (!FuncUtils.isNull(respText))
        {
            return respText;
        }
        try
        {
            InputStream resStream = request.getInputStream();
            ms = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int count;
            while ((count = resStream.read(buf, 0, buf.length)) > 0)
            {
                ms.write(buf, 0, count);
            }
            resStream.close();
            respText = new String(ms.toByteArray(), "UTF-8");
            return respText;
        } catch (IOException e)
        {

            return null;
        }
    }

    private boolean userAddMoney(JSONObject jsonObject){
        if (jsonObject == null) {
            return false;
        }
        UserUserinfo byNameLock = userUserinfoService.getByNameLock(jsonObject.getString("no_order").split("@")[1].toString());
        if(byNameLock == null){
            return true;
        }
        UserChargeLog chargeLog = userChargeLogService.getByRecordCode(jsonObject.getString("oid_order"));
        if (null != chargeLog) {
            return true;
        }
        try {
            userUserinfoService.updateUserMoney(byNameLock.getUserName(), new BigDecimal(jsonObject.getString("money_order")), jsonObject.getString("oid_order"), EnumUtil.MoneyChangeType.ALIPAY_RECHARGE);
        } catch (Exception ex) {
            logger.error("LianLianPayLog:-------" + ex.getLocalizedMessage());
            return false;
        }
        return true;
    }

    @ResponseBody
    @RequestMapping(value = "payquickPay")
    protected RetBean payquickPay(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        resp.setCharacterEncoding("UTF-8");
        System.out.println("进入支付异步通知数据接收处理");
        RetBean retBean = new RetBean();
        String reqStr = LLPayUtil.readReqStr(req);
        if (LLPayUtil.isnull(reqStr))
        {
            retBean.setRet_code("9999");
            retBean.setRet_msg("交易失败");
            resp.getWriter().write(JSON.toJSONString(retBean));
            resp.getWriter().flush();

        }
        System.out.println("接收支付异步通知数据：【" + reqStr + "】");
        try
        {
            if (!LLPayUtil.checkSign(reqStr, PartnerConfig.YT_PUB_KEY,
                    PartnerConfig.MD5_KEY))
            {
                retBean.setRet_code("9999");
                retBean.setRet_msg("交易失败");
                resp.getWriter().write(JSON.toJSONString(retBean));
                resp.getWriter().flush();
                System.out.println("支付异步通知验签失败");

            }
        } catch (Exception e)
        {
            System.out.println("异步通知报文解析异常：" + e);
            retBean.setRet_code("9999");
            retBean.setRet_msg("交易失败");
            resp.getWriter().write(JSON.toJSONString(retBean));
            resp.getWriter().flush();

        }

        // 解析异步通知对象
        PayDataBean payDataBean = JSON.parseObject(reqStr, PayDataBean.class);
        // TODO:更新订单，发货等后续处理
        boolean addMoney = userAddMoneyquickPay(payDataBean);
        System.out.println(addMoney+"--------------------------------------------------------------");
        if  (addMoney){
            retBean.setRet_code("0000");
            retBean.setRet_msg("交易成功");
            resp.getWriter().write(JSON.toJSONString(retBean));
            resp.getWriter().flush();
            System.out.println("支付异步通知数据接收处理成功");
        }else {
            retBean.setRet_code("9999");
            retBean.setRet_msg("交易失败");
            resp.getWriter().write(JSON.toJSONString(retBean));
            resp.getWriter().flush();


        }
        return retBean;
    }

    @ResponseBody
    @RequestMapping(value = "daiFuSuccess")
    protected RetBean daiFuSuccess(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        resp.setCharacterEncoding("UTF-8");
        RetBean retBean = new RetBean();
        retBean.setRet_code("0000");
        retBean.setRet_msg("交易成功");
        resp.getWriter().write(JSON.toJSONString(retBean));
        resp.getWriter().flush();
        System.out.println("支付异步通知数据接收处理成功");
        return retBean;
    }

    private boolean userAddMoneyquickPay( PayDataBean payDataBean){
        if (payDataBean == null) {
            return false;
        }
        String orderNo=payDataBean.getInfo_order();
        UserUserinfo byNameLock = userUserinfoService.getByNameLock(orderNo.split("-")[0]);
        System.out.println(byNameLock);
        if(byNameLock == null){
            return true;
        }
        UserChargeLog chargeLog = userChargeLogService.getByRecordCode(payDataBean.getNo_order());
        if (null != chargeLog) {
            return true;
        }
        try {
            userExStarService.insertLLpaylog(byNameLock,payDataBean);

        } catch (Exception ex) {
            logger.error("LianLianPayLog:-------" + ex.getLocalizedMessage());
            System.out.println( ex.getLocalizedMessage());
            return false;
        }
        return true;
    }



}
