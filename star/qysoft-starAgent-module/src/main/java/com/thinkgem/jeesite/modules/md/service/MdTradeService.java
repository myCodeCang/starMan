package com.thinkgem.jeesite.modules.md.service;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils2;
import com.thinkgem.jeesite.common.utils.VerifyUtils;
import com.thinkgem.jeesite.modules.md.dao.MdTradeDao;
import com.thinkgem.jeesite.modules.md.entity.*;
import com.thinkgem.jeesite.modules.star.entity.BaseGoodsGroup;
import com.thinkgem.jeesite.modules.star.service.BaseGoodsGroupService;
import com.thinkgem.jeesite.modules.sys.utils.UserInfoUtils;
import com.thinkgem.jeesite.modules.trans.entity.TransGoods;
import com.thinkgem.jeesite.modules.trans.service.TransGoodsService;
import com.thinkgem.jeesite.modules.user.entity.UserUserinfo;
import com.thinkgem.jeesite.modules.user.service.UserUserinfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kevin on 2017/10/2.
 */
@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackForClassName = {"RuntimeException", "Exception", "ValidationException"})
public class MdTradeService extends CrudService<MdTradeDao, MdTrade> {

    @Resource
    private UserUserinfoService userUserinfoService;
    @Resource
    private TransGoodsService transGoodsService;
    @Resource
    private BaseGoodsGroupService baseGoodsGroupService;
    @Resource
    private MdTradeMainService mdTradeMainService;



    public MdTrade get(String id) {
        return super.get(id);
    }

    public List<MdTrade> findList(MdTrade mdTrade) {
        return super.findList(mdTrade);
    }

    public Page<MdTrade> findPage(Page<MdTrade> page, MdTrade mdTrade) {
        return super.findPage(page, mdTrade);
    }


    public void save(MdTrade mdTrade) {
        super.save(mdTrade);
    }


    public void delete(MdTrade mdTrade) {
        super.delete(mdTrade);
    }

    public MdTrade getLock(String id) {
        return dao.getLock(id);
    }
    public void cancelOrder(String orderId, String userName){
        MdTrade tradeLock = dao.getLock(orderId);
        if(tradeLock == null){
            throw new ValidationException("订单不存在!");
        }
        if(!tradeLock.getUserName().equals(userName)){
            throw new ValidationException("只可下架自己的订单!");
        }
        if(tradeLock.getState()!=TradeState.USER_CANCEL.orginal()){
            dao.updateTradeState(Integer.valueOf(orderId),TradeState.USER_CANCEL.orginal());
        }
    }
    public void autoCancelOrder(String orderId,String userName)throws ValidationException{
        MdTrade mdTrade = this.getLock(orderId);
        if(mdTrade == null){
            throw new ValidationException("订单不存在!");
        }
        if(!mdTrade.getUserName().equals(userName)){
            throw new ValidationException("只可以处理自己的订单!");
        }
        //退还手续费
//        BigDecimal buyShouXu = null;
//        BigDecimal sellShouXu = null;
//        try {
//            buyShouXu = new BigDecimal(Global.getOption("md_config","buy_procedure_money"));
//            sellShouXu = new BigDecimal(Global.getOption("md_config","sell_procedure_money"));
//        } catch (Exception e) {
//            throw new ValidationException("手续费配置错误");
//        }
//        BigDecimal publishNum = new BigDecimal(mdTrade.getPublishNum());
        //处理买入
        if(String.valueOf(mdTrade.getType()).equals(TradeType.BUY_IN.toString())){
            if (EnumUtil.YesNO.YES.toString().equals(mdTrade.getRemarks())){
                userUserinfoService.updateUserMoney(userName,mdTrade.getBond(),"系统下架退还,产品编号:"+mdTrade.getGroupId()+"----单号:"+orderId, EnumUtil.MoneyChangeType.SYSTEM_CANCEL_ORDER);
                userUserinfoService.updateUserMoney(userName,mdTrade.getProfit(),"系统下架退还手续费"+"----单号:"+orderId, EnumUtil.MoneyChangeType.SYSTEM_CANCEL_ORDER);
            }else{
                userUserinfoService.updateUserOtherMoney(userName, mdTrade.getBond(), EnumUtil.MoneyType.money2,"系统下架退还,产品编号:"+mdTrade.getGroupId()+"----单号:"+orderId, EnumUtil.MoneyChangeType.SYSTEM_CANCEL_ORDER);
                userUserinfoService.updateUserOtherMoney(userName,mdTrade.getProfit(),EnumUtil.MoneyType.money2,"系统下架退还手续费"+"----单号:"+orderId, EnumUtil.MoneyChangeType.SYSTEM_CANCEL_ORDER);
            }
            dao.updateProfit(BigDecimal.ZERO,orderId);
            dao.updateBond(BigDecimal.ZERO,orderId);
        }
        //处理卖出
        if(String.valueOf(mdTrade.getType()).equals(TradeType.SELL_OUT.toString())){
            transGoodsService.updateGoodsNum(userName,mdTrade.getGroupId(),mdTrade.getRemainNum());
        }
        dao.updateRemainNum(0,orderId);
        if(mdTrade.getState() ==  TradeState.ON_TRADING.orginal()){
            dao.updateTradeState(Integer.valueOf(orderId),TradeState.SYSTEM_OFF.orginal());
        }
    }

    public void publishBuy(String userName, String groupId, BigDecimal money, int num,String payMethod)throws ValidationException {
        UserUserinfo userinfo = userUserinfoService.getByNameLock(userName);
        BigDecimal numDecimal;
        try {
            numDecimal = BigDecimal.valueOf(num);
        } catch (Exception e) {
            throw new ValidationException("发布求购数量错误");
        }
        //判断商品存在
        BaseGoodsGroup baseGoodsGroup = baseGoodsGroupService.get(groupId);
        //判断是否可以进行撮合交易
        if (null == baseGoodsGroup)  {
            throw new ValidationException("当前产品不存在,不可发布");
        }
        if (!GoodsStatus.MD.toString().equals(baseGoodsGroup.getStatus())){
            throw new ValidationException("当前产品已过期,请前往持仓商城进行交易!",1006);
        }
        //判断是否在交易时间内
        if(validateIsTransTime() == false){
            throw new ValidationException("不在交易时间内");
        }
        //判断是否是交易日
        validateIsTransDay();
        if (money.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("产品求购价格必须大于0");
        }
        if (!VerifyUtils.isInteger(numDecimal)) {
            throw new ValidationException("时间求购数量必须为正整数");
        }
        //判断涨幅状况
//        valiIsOverTop(groupId);
        //判断交易价格是否在区间内
        Map<String, Object> vailIsRationalPrice = vailIsRationalPrice(groupId);
        if (money.compareTo(new BigDecimal(vailIsRationalPrice.get("hightestPrice").toString()))>0){
            throw new ValidationException("发布价格过高");
        }
        if (money.compareTo(new BigDecimal(vailIsRationalPrice.get("lowestPrice").toString()))<0){
            throw new ValidationException("发布价格过低");
        }
        //买家手续费-实际得到
        BigDecimal buyShouXu = null;
        try {
            buyShouXu = new BigDecimal(Global.getOption("md_config","buy_procedure_money"));
        } catch (Exception e) {
            throw new ValidationException("买家手续费配置错误");
        }
//        BigDecimal allMoney = money.multiply(numDecimal).multiply(BigDecimal.ONE.add(buyShouXu)).setScale(2,BigDecimal.ROUND_HALF_UP);
        BigDecimal useMoney = money.multiply(numDecimal).setScale(2,BigDecimal.ROUND_HALF_UP);
        BigDecimal shouXuMoney = useMoney.multiply(buyShouXu).setScale(2,BigDecimal.ROUND_HALF_UP);
        BigDecimal allMoney = useMoney.add(shouXuMoney);
        String remarks;
        if ("fund".equals(payMethod)){
            remarks = "2";
            if (userinfo.getMoney2().compareTo(allMoney) < 0) {
                throw new ValidationException("用户资金账户余额不足,共需金额:"+allMoney+"元");
            }
            //扣除用户金钱
            userUserinfoService.updateUserOtherMoney(userName, useMoney.multiply(new BigDecimal(-1)).setScale(2,BigDecimal.ROUND_HALF_UP), EnumUtil.MoneyType.money2,"求购产品成功", EnumUtil.MoneyChangeType.MDTradeBuy);
            userUserinfoService.updateUserOtherMoney(userName,shouXuMoney.multiply(new BigDecimal(-1)).setScale(2,BigDecimal.ROUND_HALF_UP),EnumUtil.MoneyType.money2,"求购产品手续费", EnumUtil.MoneyChangeType.MdTrade_FEE);
        }
        else{
             remarks = "1";
            if (userinfo.getMoney().compareTo(allMoney) < 0) {
                throw new ValidationException("用户出金账户余额不足,共需金额:"+allMoney+"元");
            }
            //扣除用户金钱
            userUserinfoService.updateUserMoney(userName, useMoney.multiply(new BigDecimal(-1)).setScale(2,BigDecimal.ROUND_HALF_UP), "求购产品成功", EnumUtil.MoneyChangeType.MDTradeBuy);
            userUserinfoService.updateUserMoney(userName,shouXuMoney.multiply(new BigDecimal(-1)).setScale(2,BigDecimal.ROUND_HALF_UP),"求购产品手续费", EnumUtil.MoneyChangeType.MdTrade_FEE);
        }
        MdTrade mdTrade = new MdTrade();
        mdTrade.setGroupId(groupId);
        mdTrade.setUserId(userinfo.getId());
        mdTrade.setUserName(userName);
        mdTrade.setPrice(money);
        mdTrade.setPublishNum(num);
        mdTrade.setRemainNum(num);
        mdTrade.setType(TradeType.BUY_IN.orignal());
        mdTrade.setState(TradeState.ON_TRADING.orginal());
        mdTrade.setLaststate(1);
        mdTrade.setBond(useMoney);
        mdTrade.setFee(buyShouXu);
        mdTrade.setProfit(shouXuMoney);
        mdTrade.setRemarks(remarks);
        this.save(mdTrade);
    }

    public void publishSell(String userName, String groupId, BigDecimal money, int num,String payMethod) {
        UserUserinfo userinfo = userUserinfoService.getByNameLock(userName);
        //判断商品存在
        BaseGoodsGroup baseGoodsGroup = baseGoodsGroupService.get(groupId);
        if (null == baseGoodsGroup) {
            throw new ValidationException("当前交易品不存在,不可发布");
        }
        if(userinfo == null){
            throw new ValidationException("操作用户不存在");
        }
        //判断是否可以进行撮合交易
        if (!GoodsStatus.MD.toString().equals(baseGoodsGroup.getStatus())){
            throw new ValidationException("当前产品已过期,请前往持仓商城进行交易!",1006);
        }
        if (money.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("交易品发布价格必须大于0");
        }
        if (num<0) {
            throw new ValidationException("交易品发布数量必须为正整数");
        }
        //判断是否在交易时间内
        if(validateIsTransTime() == false){
            throw new ValidationException("不在交易时间内");
        }
        //判断是否是交易日
        validateIsTransDay();
        //判断涨幅状况
//        valiIsOverTop(groupId);
        //判断价格区间
        Map<String, Object> vailIsRationalPrice = vailIsRationalPrice(groupId);
        if (money.compareTo(new BigDecimal(vailIsRationalPrice.get("hightestPrice").toString()))>0){
            throw new ValidationException("发布价格过高");
        }
        if (money.compareTo(new BigDecimal(vailIsRationalPrice.get("lowestPrice").toString()))<0){
            throw new ValidationException("发布价格过低");
        }
        //判断用户交易品充足
        TransGoods transGoods = transGoodsService.getUserStar(groupId,userName);
        if(transGoods == null){
            throw new ValidationException("持有量不足，发布转让失败。");
        }
        TransGoods transGoodsSell = transGoodsService.getLock(transGoods.getId());
        // 修改用户交易品持有状态  , 发布交易
        if(transGoodsSell.getNum() < num ){
            throw new ValidationException("持有量不足，发布转让失败。");
        }
        transGoodsService.updateNum(transGoodsSell.getId(),-num);
        //扣除手续费
        BigDecimal sellShouXu;
        try {
            sellShouXu = new BigDecimal(Global.getOption("md_config","sell_procedure_money"));
        } catch (Exception e) {
            throw new ValidationException("卖家手续费配置错误");
        }
//        BigDecimal shouXu = money.multiply(new BigDecimal(num).multiply(sellShouXu));
//        BigDecimal feePrice = money.multiply(sellShouXu);
//        if ("balance".equals(payMethod)){
//            if(userinfo.getMoney().compareTo(shouXu)<0){
//                throw new ValidationException("账户余额不足,请充值!");
//            }
//            userUserinfoService.updateUserMoney(userName,shouXu.multiply(BigDecimal.valueOf(-1)).setScale(2,BigDecimal.ROUND_HALF_UP),"用户发布转让扣除手续费",EnumUtil.MoneyChangeType.MdTrade_FEE);
//        }
//        if("fund".equals(payMethod)){
//            if(userinfo.getMoney2().compareTo(shouXu)<0){
//                throw new ValidationException("资金账户余额不足,请充值!");
//            }
//            userUserinfoService.updateUserOtherMoney(userName,shouXu.multiply(BigDecimal.valueOf(-1)).setScale(2,BigDecimal.ROUND_HALF_UP),EnumUtil.MoneyType.money2,"用户发布转让扣除手续费",EnumUtil.MoneyChangeType.MdTrade_FEE);
//        }
        //发布交易表信息
        MdTrade mdTrade = new MdTrade();
        mdTrade.setGroupId(groupId);
        mdTrade.setUserId(userinfo.getId());
        mdTrade.setUserName(userName);
        mdTrade.setPrice(money);
        mdTrade.setPublishNum(num);
        mdTrade.setRemainNum(num);
        mdTrade.setType(TradeType.SELL_OUT.orignal());
        mdTrade.setState(TradeState.ON_TRADING.orginal());
        mdTrade.setLaststate(1);
        mdTrade.setBond(BigDecimal.ZERO);
        mdTrade.setFee(sellShouXu);
        mdTrade.setProfit(BigDecimal.ZERO);
        this.save(mdTrade);
    }
    public boolean validateIsTransTime() throws ValidationException {
        Date beginTime;
        Date endTime;
        Date beginTime2;
        Date endTime2;
        Date beginTime3;
        Date endTime3;
        try {
            //判断是否在交易时间内
            beginTime = DateUtils2.formatStrTime(Global.getOption("md_config", "md_time_begin","00:00:00"));
            endTime = DateUtils2.formatStrTime(Global.getOption("md_config", "md_time_end","00:00:00"));

            beginTime2 = DateUtils2.formatStrTime(Global.getOption("md_config", "md_time_begin2","00:00:00"));
            endTime2 = DateUtils2.formatStrTime(Global.getOption("md_config", "md_time_end2",DateUtils2.getTime()));

            beginTime3 = DateUtils2.formatStrTime(Global.getOption("md_config", "md_time_begin3","00:00:00"));
            endTime3 = DateUtils2.formatStrTime(Global.getOption("md_config", "md_time_end3",DateUtils2.getTime()));
        } catch (Exception e) {
            throw new ValidationException("交易时间配置错误!");
        }
        Date nowtime = DateUtils2.formatStrTime(DateUtils2.getTime());
        if (!( nowtime.getTime() > beginTime.getTime() && nowtime.getTime() < endTime.getTime())&& !(nowtime.getTime() > beginTime2.getTime() && nowtime.getTime() < endTime2.getTime())&& !(nowtime.getTime() > beginTime3.getTime() && nowtime.getTime() < endTime3.getTime())){
            return false;
        }else {
            return true;
        }
    }


    public void validateIsTransDay() throws ValidationException {
        //判断是否是交易日
        String week = DateUtils2.getWeekOfDate(new Date());
        if(!"on".equals(Global.getOption("md_config",week))){
            throw new ValidationException("抱歉,不在交易日内!");
        }
    }

    public void valiIsOverTop(String groupId) throws ValidationException{
        //判断涨幅状况
        MdTradeMain nowMoney = mdTradeMainService.getNowMoney(groupId);
        if (EnumUtil.YesNO.YES.toString().equals(nowMoney.getIsOverTop())){
            throw new ValidationException("交易已停止!");
        }
    }

    public Map<String,Object> vailIsRationalPrice(String groupId) throws ValidationException{
        //判断价格是否合理
        BigDecimal hightestPrice;
        BigDecimal lowestPrice;
        BigDecimal upFloat;
        BigDecimal downFloat;
        BigDecimal firstUpFloat;
        BigDecimal firstDownFloat ;
        BigDecimal openUpFloat ;
        BigDecimal openDownFloat ;
        try {
            firstUpFloat = new BigDecimal(Global.getOption("md_config","first_md_up_float"));
            firstDownFloat = new BigDecimal(Global.getOption("md_config","first_md_down_float"));
            openUpFloat = new BigDecimal(Global.getOption("md_config","open_md_up_float"));
            openDownFloat = new BigDecimal(Global.getOption("md_config","open_md_down_float"));
        } catch (Exception e) {
            throw new ValidationException("价格波动配置错误");
        }
        MdTradeMain mdTradeMain = mdTradeMainService.getNowMoney(groupId);
        if(mdTradeMain == null){
            throw new ValidationException("获取最新价失败");
        }
        BigDecimal openPrice = mdTradeMain.getOpeningPrice();
        BaseGoodsGroup baseGoodsGroup = baseGoodsGroupService.get(groupId);
        if (baseGoodsGroup==null){
            throw new ValidationException("所购明星不存在！");
        }
        //判断是否是首日
        Date publishDate = baseGoodsGroup.getTransStartTime();
        String firstDay = DateUtils2.formatDate(publishDate, "yyyy-MM-dd");
        String now = DateUtils2.formatDate(new Date(),"yyyy-MM-dd");
        if(firstDay.equals(now)){
            upFloat = firstUpFloat;
            downFloat = firstDownFloat;
        }else{
            upFloat = openUpFloat;
            downFloat = openDownFloat;
        }
        hightestPrice = openPrice.multiply(BigDecimal.ONE.add(upFloat)).setScale(3,BigDecimal.ROUND_HALF_UP);;
        lowestPrice = openPrice.multiply(BigDecimal.ONE.subtract(downFloat)).setScale(3,BigDecimal.ROUND_HALF_UP);;
        Map<String,Object> map = new HashMap<>();
        map.put("hightestPrice",hightestPrice);
        map.put("lowestPrice",lowestPrice);
        return map;
    }

    private List<MdTrade> groupListLimit(String groupId, int top, boolean isBuy){
        String type="1";
        String order = "a.price desc";
        if(!isBuy){
            order = "a.price asc";
            type = "2";
        }
        List<MdTrade> mdTradeList =  dao.getBuyGroupListLimit(groupId, "4", type, order, top,new Date());

        return mdTradeList;
    }
    /**
     * 查询卖出
     *
     * @param groupId
     * @param top
     * @return
     */
    public List<MdTrade> getSellGroupListLimit(String groupId, int top) {

        return this.groupListLimit(groupId,  top,false);
    }

    /**
     * 查询求购
     *
     * @param groupId
     * @param top
     * @return
     */
    public List<MdTrade> getBuyGroupListLimit(String groupId, int top) {
        return this.groupListLimit(groupId,  top,true);
    }


    public MdTrade getSellOnePrice(String groupId, String type) {
       return dao.getSellOnePrice(groupId,type);
    }
}
