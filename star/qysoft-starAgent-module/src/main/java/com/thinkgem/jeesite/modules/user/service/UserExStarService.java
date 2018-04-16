package com.thinkgem.jeesite.modules.user.service;


import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.utils.VerifyUtils;
import com.thinkgem.jeesite.modules.lianlianPay.entity.PayDataBean;
import com.thinkgem.jeesite.modules.lianlianPay.service.LianlianPayService;
import com.thinkgem.jeesite.modules.user.dao.UserAccountchangeDao;
import com.thinkgem.jeesite.modules.user.dao.UserChargeLogDao;
import com.thinkgem.jeesite.modules.user.dao.UserUserinfoDao;
import com.thinkgem.jeesite.modules.user.dao.UserWithdrawDao;
import com.thinkgem.jeesite.modules.user.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * ,用户业务层
 */
@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackForClassName = {"RuntimeException", "Exception", "ValidationException"})
public class UserExStarService {
    @Resource
    private UserUserinfoDao userUserinfoDao;

    @Resource
    private UserTeamLevelService userTeamLevelService;

    @Autowired
    private UserChargeLogDao userChargeLogDao;

    @Resource
    private UserWithdrawService userWithdrawService;

    @Resource
    private UserUserinfoService userUserinfoService;

    @Resource
    private UserWithdrawDao userWithdrawDao;

    @Resource
    private LianlianPayService lianlianPayService;


    public void updateUserType(String id, String userType, String levelNumber) {
        UserUserinfo user = userUserinfoDao.get(id);
        if(user == null){
            throw new ValidationException("升级的用户不存在");
        }
        if(EnumUtil.YesNO.YES.toString().equals(user.getIsShop())){
            throw new ValidationException("该用户已被指定为经纪人,不可再设置为代理");
        }
        UserUserinfo parentUser = userUserinfoDao.getByName(user.getParentName());
        if(parentUser == null){
            throw new ValidationException("上级用户不存在");
        }
        if(!VerifyUtils.isNumeric(levelNumber)){
            throw new ValidationException("号段类型错误");
        }
        if("00".equals(levelNumber)){
            throw new ValidationException("号段类型不可是00");
        }
        UserUserinfo byShopId = null;
        switch (userType){
            case "3":
                if(!"1".equals(parentUser.getId())){
                    throw new ValidationException("没有升级运营中心权限");
                }
                if(levelNumber.length()!=5){
                    throw new ValidationException("号段位数必须为5位");
                }
                if("0".equals(levelNumber.substring(4))){
                    throw new ValidationException("号段不能以0结尾");
                }
                byShopId = userUserinfoDao.getByShopId(levelNumber);
                break;
            case "2":
                if(!"3".equals(parentUser.getUserType())){
                    throw new ValidationException("没有升级一级代理权限");
                }
                if(levelNumber.length()!=2){
                    throw new ValidationException("号段位数必须为2位");
                }
                levelNumber = parentUser.getShopId()+levelNumber;
                byShopId = userUserinfoDao.getByShopId(levelNumber);
                break;
            case "1":
                if(!"2".equals(parentUser.getUserType())){
                    throw new ValidationException("没有升级二级代理权限");
                }
                if(levelNumber.length()!=2){
                    throw new ValidationException("号段位数必须为2位");
                }
                levelNumber = parentUser.getShopId()+levelNumber;
                byShopId = userUserinfoDao.getByShopId(levelNumber);
                break;
            default:
                levelNumber = StringUtils2.EMPTY;
                break;
        }
        if(byShopId != null){
            throw new ValidationException("该号段已有用户使用");
        }
        if(StringUtils2.isBlank(levelNumber)){
            throw new ValidationException("升级失败!");
        }
        userUserinfoDao.updateUserType(user.getUserName(),userType);
        userUserinfoDao.updateShopId(user.getUserName(),levelNumber);
    }

    public String  normalUserNumber(String parteName){
        StringBuilder stringBuilder = new StringBuilder();
        UserUserinfo byName = userUserinfoDao.getByName(parteName);
        if(byName == null){
            throw new ValidationException("上级用户不存在!");
        }
        List<UserUserinfo> list = userUserinfoDao.getUsersByParentName(parteName);
        String userNum = String.valueOf(list.size()+1);
        stringBuilder.append(byName.getShopId());
        int length = userNum.length()+stringBuilder.length();
        for(int i=0;i<15-length;i++){
            stringBuilder.append('0');
        }
        stringBuilder.append(userNum);
        return stringBuilder.toString();
    }

    public void updateUserIsUsercenter(String userName,String shopId, String isUsercenter) {
        String number = IdGen.uuid("seq_manageNum");
        userUserinfoDao.updateUserZhuanqupass(userName,shopId);
        userUserinfoDao.updateShopId(userName,number);
        userUserinfoDao.updateUserIsUsercenter(userName,isUsercenter);
    }

    public void backoutCenter(UserUserinfo user, String isUsercenter) {
        userUserinfoDao.updateShopId(user.getUserName(),user.getZhuanquPass());
        userUserinfoDao.updateUserIsUsercenter(user.getUserName(),isUsercenter);
    }

    public void saveUserWithdraw(UserWithdraw withdraw, String status, String message){
        UserWithdraw userWithdraw = userWithdrawService.get(withdraw.getId());
        if (userWithdraw == null){
            throw new ValidationException ("审核有误");
        }
        if(!userWithdraw.getStatus ().equals (EnumUtil.CheckType.uncheck.toString ())){
            throw new ValidationException ("已审核过,不要重复审核");
        }
        BigDecimal money;
        try {
            BigDecimal poundage = new BigDecimal(Global.getOption("system_user_set", "poundage"));
            money = new BigDecimal(userWithdraw.getChangeMoney()).multiply(BigDecimal.ONE.subtract(poundage));
        } catch (Exception e) {
            throw new ValidationException ("手续费配置错误!");
        }

        if(status.equals (EnumUtil.YesNO.NO.toString ())){
            userWithdraw.setStatus(EnumUtil.CheckType.failed.toString ());
            userUserinfoService.updateUserMoney (userWithdraw.getUserName (),new BigDecimal(userWithdraw.getChangeMoney ()),"提现驳回退款,驳回原因: "+message,EnumUtil.MoneyChangeType.widthdrawReject);
        }
        else if(status.equals (EnumUtil.YesNO.YES.toString ())){

            //打款操作
            boolean reast = false;

            reast = lianlianPayService.orderToPay(userWithdraw, money.setScale(2, BigDecimal.ROUND_HALF_UP));

            if (!reast){
                throw new ValidationException("打款失败,请确认打款信息是否正确");
            }
            userWithdraw.setStatus(EnumUtil.CheckType.success.toString ());
        }
        userWithdraw.setUpdateDate (new Date ());
        userWithdraw.setCommt(message);
        userWithdrawDao.updateStatus (userWithdraw);
    }

    public  void  insertLLpaylog(UserUserinfo byNameLock,PayDataBean payDataBean ){

        UserChargeLog userChargeLog = new UserChargeLog();
        userChargeLog.setUserName(byNameLock.getUserName());
        userChargeLog.setRecordcode(payDataBean.getNo_order());
        userChargeLog.setChangeMoney(payDataBean.getMoney_order());
        userChargeLog.setBeforeMoney(byNameLock.getMoney());
        userChargeLog.setStatus(EnumUtil.YesNO.YES.toString());
        userChargeLog.setAfterMoney(byNameLock.getMoney().add( new BigDecimal(payDataBean.getMoney_order())).toString());
        userChargeLog.setChangeFrom("快捷支付充值");
        userChargeLog.setChangeType(EnumUtil.MoneyType.money.toString());
        userChargeLog.setCommt(payDataBean.getNo_order() + "-" + byNameLock.getUserName());
        userChargeLog.preInsert();
        userChargeLogDao.insert(userChargeLog);
        userUserinfoService.updateUserMoney(byNameLock.getUserName(), new BigDecimal(payDataBean.getMoney_order()),payDataBean.getNo_order(), EnumUtil.MoneyChangeType.QUICKPay_RECHARGE);

    }
}
