package com.thinkgem.jeesite.modules.lianlianPay.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lianlianpay.security.utils.LianLianPaySecurity;
import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.utils.LogAppendHelper;
import com.thinkgem.jeesite.modules.lianlianPay.constant.PaymentConstant;
import com.thinkgem.jeesite.modules.lianlianPay.constant.PaymentStatusEnum;
import com.thinkgem.jeesite.modules.lianlianPay.constant.RetCodeEnum;
import com.thinkgem.jeesite.modules.lianlianPay.entity.PaymentRequestBean;
import com.thinkgem.jeesite.modules.lianlianPay.entity.PaymentResponseBean;
import com.thinkgem.jeesite.modules.lianlianPay.entity.QueryPaymentRequestBean;
import com.thinkgem.jeesite.modules.lianlianPay.entity.QueryPaymentResponseBean;
import com.thinkgem.jeesite.modules.lianlianPay.until.DateUtil;
import com.thinkgem.jeesite.modules.lianlianPay.until.HttpUtil;
import com.thinkgem.jeesite.modules.lianlianPay.until.SignUtil;
import com.thinkgem.jeesite.modules.lianlianPay.until.TraderRSAUtil;
import com.thinkgem.jeesite.modules.payment.entity.PaymentCallback;
import com.thinkgem.jeesite.modules.user.entity.UserChargeLog;
import com.thinkgem.jeesite.modules.user.entity.UserWithdraw;
import com.thinkgem.jeesite.modules.user.service.UserChargeLogService;
import com.thinkgem.jeesite.modules.user.service.UserUserinfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.RollingFileAppender;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Administrator on 2017/7/28.
 */
@Service
public class LianlianPayService {

    private Logger logger = getLogger();

    public boolean orderToPay(UserWithdraw userWithdraw, BigDecimal money)  {
        logger.error("用户:"+userWithdraw.getUserName()+",订单编号:"+userWithdraw.getRecordcode()+"打款开始start-----------");
        // 连连内部测试环境数据(商户测试期间需要用正式的数据测试，测试时默认单笔单日单月额度50，等测试OK，申请走上线流程打开额度）
        PaymentRequestBean paymentRequestBean = new PaymentRequestBean();
        paymentRequestBean.setNo_order(userWithdraw.getRecordcode());
        paymentRequestBean.setDt_order(DateUtil.getCurrentDateTimeStr1());
        paymentRequestBean.setMoney_order(money.toString());
        paymentRequestBean.setCard_no(userWithdraw.getUserBankNum());
        paymentRequestBean.setAcct_name(userWithdraw.getUserBankName());
        // paymentRequestBean.setBank_name("中国平安银行");
        paymentRequestBean.setInfo_order("用户提现");
        paymentRequestBean.setFlag_card("0");
        paymentRequestBean.setMemo("代付");
        // 填写商户自己的接收付款结果回调异步通知
        String notifyUrl = Global.getOption("system_sys", "domain");
        paymentRequestBean.setNotify_url(notifyUrl+"/payment/lianlianPay/daiFuSuccess");
        paymentRequestBean.setOid_partner(PaymentConstant.OID_PARTNER);
        // paymentRequestBean.setPlatform("test.com");
        paymentRequestBean.setApi_version(PaymentConstant.API_VERSION);
        paymentRequestBean.setSign_type(PaymentConstant.SIGN_TYPE);
        // 用商户自己的私钥加签
        paymentRequestBean.setSign(SignUtil.genRSASign(JSON.parseObject(JSON.toJSONString(paymentRequestBean))));
        String jsonStr = JSON.toJSONString(paymentRequestBean);
        logger.error("实时付款请求报文：" + jsonStr);
        // 用银通公钥对请求参数json字符串加密
        // 报Illegal key
        // size异常时，可参考这个网页解决问题http://www.wxdl.cn/java/security-invalidkey-exception.html
        String encryptStr = null;
        try {
            encryptStr = LianLianPaySecurity.encrypt(jsonStr, PaymentConstant.PUBLIC_KEY_ONLINE);
        } catch (Exception e) {
            logger.error("加密异常:");
            return false;
        }
        if (StringUtils.isEmpty(encryptStr)) {
            // 加密异常
            logger.error("加密异常:");
            return false;
        }

        JSONObject json = new JSONObject();
        json.put("oid_partner", PaymentConstant.OID_PARTNER);
        json.put("pay_load", encryptStr);
        String response = HttpUtil.doPost("https://instantpay.lianlianpay.com/paymentapi/payment.htm", json, "UTF-8");
        System.out.print("付款接口返回响应报文：" + response);
        logger.error("付款接口返回响应报文：" + response);
        if (StringUtils.isEmpty(response)) {
            // 出现异常时调用订单查询，明确订单状态，不能私自设置订单为失败状态，以免造成这笔订单在连连付款成功了，而商户设置为失败
            queryPaymentAndDealBusiness(paymentRequestBean.getNo_order());
        } else {
            PaymentResponseBean paymentResponseBean = JSONObject.parseObject(response, PaymentResponseBean.class);
            // 对返回0000时验证签名
            if (paymentResponseBean.getRet_code().equals("0000")) {
                // 先对结果验签
                boolean signCheck = TraderRSAUtil.checksign(PaymentConstant.PUBLIC_KEY_ONLINE,
                        SignUtil.genSignData(JSONObject.parseObject(response)), paymentResponseBean.getSign());
                if (!signCheck) {
                    // 传送数据被篡改，可抛出异常，再人为介入检查原因
                    logger.error("返回结果验签异常,可能数据被篡改");
                    return false;
                }
                logger.info(paymentRequestBean.getNo_order() + "订单处于付款处理中");
                // 已生成连连支付单，付款处理中（交易成功，不是指付款成功，是指跟连连流程正常），商户可以在这里处理自已的业务逻辑（或者不处理，在异步回调里处理逻辑）,最终的付款状态由异步通知回调告知
            } else if (paymentResponseBean.getRet_code().equals("4002")
                    || paymentResponseBean.getRet_code().equals("4004")) {

                throw new ValidationException(paymentResponseBean.getRet_msg());
                // 当调用付款接口返回4002，4003，4004,会同时返回验证码，用于确认付款接口
                // 对于疑似重复订单，需先人工审核这笔订单是否正常的付款请求，而不是系统产生的重复订单，确认后再调用确认付款接口或者在连连商户站后台操作疑似订单，api不调用确认付款接口
                // 对于疑似重复订单，也可不做处理，
                // TODO
            } else if (RetCodeEnum.isNeedQuery(paymentResponseBean.getRet_code())) {
                // 出现1002，2005，4006，4007，4009，9999这6个返回码时（或者对除了0000之后的code都查询一遍查询接口）调用订单查询，明确订单状态，不能私自设置订单为失败状态，以免造成这笔订单在连连付款成功了，而商户设置为失败
                // 第一次测试对接时，返回{"ret_code":"4007","ret_msg":"敏感信息解密异常"},可能原因报文加密用的公钥改动了,demo中的公钥是连连公钥，商户生成的公钥用于上传连连商户站用于连连验签，生成的私钥用于加签
                queryPaymentAndDealBusiness(paymentRequestBean.getNo_order());
                throw new ValidationException(paymentResponseBean.getRet_msg());
            } else {
                // 返回其他code时，可将订单置为失败
                // TODO
                //System.out.println("failure");
                return false;
            }
        }
        return true;
    }


    // 异常时，先查询订单状态，再根据订单状态处理业务逻辑
    public static void queryPaymentAndDealBusiness(String orderNo) throws ValidationException {
        // 连连内部测试环境数据
        QueryPaymentRequestBean queryRequestBean = new QueryPaymentRequestBean();
        queryRequestBean.setNo_order(orderNo);
        queryRequestBean.setOid_partner(PaymentConstant.OID_PARTNER);
        // queryRequestBean.setPlatform("test.com");
        queryRequestBean.setApi_version(PaymentConstant.API_VERSION);
        queryRequestBean.setSign_type(PaymentConstant.SIGN_TYPE);
        queryRequestBean.setSign(SignUtil.genRSASign(JSON.parseObject(JSON.toJSONString(queryRequestBean))));
        String queryResult = HttpUtil.doPost("https://instantpay.lianlianpay.com/paymentapi/queryPayment.htm",
                JSON.parseObject(JSON.toJSONString(queryRequestBean)), "UTF-8");

        if (StringUtils.isEmpty(queryResult)) {
            // 可抛异常，查看原因
            throw new ValidationException("实时付款查询接口响应异常");
        }
        QueryPaymentResponseBean queryPaymentResponseBean = JSONObject.parseObject(queryResult,
                QueryPaymentResponseBean.class);

        // 先对结果验签
        boolean signCheck = TraderRSAUtil.checksign(PaymentConstant.PUBLIC_KEY_ONLINE,
                SignUtil.genSignData(JSONObject.parseObject(queryResult)), queryPaymentResponseBean.getSign());
        if (!signCheck) {
            // 传送数据被篡改，可抛出异常，再人为介入检查原因
            throw new ValidationException("返回结果验签异常,可能数据被篡改");
        }
        if (queryPaymentResponseBean.getRet_code().equals("0000")) {
            PaymentStatusEnum paymentStatusEnum = PaymentStatusEnum
                    .getPaymentStatusEnumByValue(queryPaymentResponseBean.getResult_pay());
            // TODO商户根据订单状态处理自已的业务逻辑
            switch (paymentStatusEnum) {
                case PAYMENT_APPLY:
                    // 付款申请，这种情况一般不会发生，如出现，请直接找连连技术处理
                    break;
                case PAYMENT_CHECK:
                    // 复核状态 TODO
                    // 返回4002，4003，4004时，订单会处于复核状态，这时还未创建连连支付单，没提交到银行处理，如需对该订单继续处理，需商户先人工审核这笔订单是否是正常的付款请求，没问题后再调用确认付款接口
                    // 如果对于复核状态的订单不做处理，可当做失败订单
                    break;
                case PAYMENT_SUCCESS:
                    // 成功 TODO
                    break;
                case PAYMENT_FAILURE:
                    // 失败 TODO
                    break;
                case PAYMENT_DEALING:
                    // 处理中 TODO
                    break;
                case PAYMENT_RETURN:
                    // 退款 TODO
                    // 可当做失败（退款情况
                    // 极小概率下会发生，个别银行处理机制是先扣款后打款给用户时再检验卡号信息是否正常，异常时会退款到连连商户账上）
                    break;
                case PAYMENT_CLOSED:
                    // 关闭 TODO 可当做失败 ，对于复核状态的订单不做处理会将订单关闭
                    break;
                default:
                    break;
            }
        } else if (queryPaymentResponseBean.getRet_code().equals("8901")) {
            // 订单不存在，这种情况可以用原单号付款，最好不要换单号，如换单号，在连连商户站确认下改订单是否存在，避免系统并发时返回8901，实际有一笔订单
        } else {
            // 查询异常（极端情况下才发生,对于这种情况，可人工介入查询，在连连商户站查询或者联系连连客服，查询订单状态）
            throw new ValidationException("查询异常");
        }

    }

    public Logger getLogger() {
        Logger logger = org.apache.log4j.Logger.getLogger(getClass());
        Optional<RollingFileAppender> rollingFileAppender = LogAppendHelper.getRollingFileAppender(getClass().getName());
        if (rollingFileAppender.isPresent()) {
            logger.addAppender(rollingFileAppender.get());
        }
        return logger;
    }
}
