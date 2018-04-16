/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.service;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils2;
import com.thinkgem.jeesite.common.utils.VerifyUtils;
import com.thinkgem.jeesite.modules.md.entity.GoodsStatus;
import com.thinkgem.jeesite.modules.md.service.MdTradeMainService;
import com.thinkgem.jeesite.modules.star.entity.BaseGoodsGroup;
import com.thinkgem.jeesite.modules.star.entity.StarProject;
import com.thinkgem.jeesite.modules.star.service.BaseGoodsGroupService;
import com.thinkgem.jeesite.modules.star.service.SaleByAuctionService;
import com.thinkgem.jeesite.modules.star.service.StarProjectService;
import com.thinkgem.jeesite.modules.trans.dao.TransBuyDao;
import com.thinkgem.jeesite.modules.trans.entity.TransBuy;
import com.thinkgem.jeesite.modules.trans.entity.TransBuyLog;
import com.thinkgem.jeesite.modules.trans.entity.TransGoods;
import com.thinkgem.jeesite.modules.trans.entity.TransOrder;
import com.thinkgem.jeesite.modules.user.entity.UserUserinfo;
import com.thinkgem.jeesite.modules.user.service.UserUserinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 产品交易大厅Service
 *
 * @author luo
 * @version 2017-05-10
 */
@Service
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class  TransBuyService extends CrudService<TransBuyDao, TransBuy> {



    @Autowired
    private TransBuyLogService transBuyLogService;

    @Autowired
    private UserUserinfoService userUserinfoService;

    @Autowired
    private BaseGoodsGroupService baseGoodsGroupService;

    @Autowired
    private SaleByAuctionService saleByAuctionService ;

    @Autowired
    private TransOrderService transOrderService ;

    @Autowired
    private TransGoodsService transGoodsService ;

    @Autowired
    private StarProjectService starProjectService ;

    @Autowired
    private MdTradeMainService mdTradeMainService ;

    public TransBuy get(String id) {
        return super.get(id);
    }

    public List<TransBuy> findList(TransBuy transBuy) {
        return super.findList(transBuy);
    }

    public Page<TransBuy> findPage(Page<TransBuy> page, TransBuy transBuy) {
        return super.findPage(page, transBuy);
    }


    public void save(TransBuy transBuy) throws ValidationException {
        super.save(transBuy);
    }


    public void delete(TransBuy transBuy) throws ValidationException {
        super.delete(transBuy);
    }

    /**
     * 锁表查询交易数据
     * @param transBuy
     * @return
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ,readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
    public List<TransBuy> findListLock(TransBuy transBuy) {
        return dao.findListLock(transBuy);
    }


    private  List<TransBuy> groupListLimit(String groupId, int top,boolean isBuy){

        String type="1";
        String order = "a.money desc";
        if(isBuy){
            type = "2";
            order = "a.money asc";
        }
        List<TransBuy> transBuyList =  dao.getBuyGroupListLimit(groupId, "1", type, order, top);

        return transBuyList;
    }

    /**
     * 查询卖出
     *
     * @param groupId
     * @param top
     * @return
     */
    public List<TransBuy> getSellGroupListLimit(String groupId, int top) {

        return this.groupListLimit(groupId,  top,false);
    }

    /**
     * 查询求购
     *
     * @param groupId
     * @param top
     * @return
     */
    public List<TransBuy> getBuyGroupListLimit(String groupId, int top) {
        return this.groupListLimit(groupId,  top,true);
    }

    /**
     * 发布卖出
     *
     * @param
     * @param money
     * @param num
     * @return
     */

    public void publishSell(String userName, String groupId, BigDecimal money, int num) throws ValidationException {
        //判断发布价格
        canPublish(groupId,money);
        //判断是否可交易
        isTransGroup(groupId);
        //判断商品存在
        BaseGoodsGroup baseGoodsGroup = baseGoodsGroupService.get(groupId);
        if (null == baseGoodsGroup) {
            throw new ValidationException("当前交易品不存在,不可发布");
        }

        if (money.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("交易品发布价格必须大于0");
        }

        if (num<0) {
            throw new ValidationException("交易品发布数量必须为正整数");
        }
        //判断是否在交易时间内
        if(!validateIsTransTime()){
            throw new ValidationException("交易未开始或者不在交易时间内。");
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
        //发布交易表信息
        TransBuy transBuy = new TransBuy();
        transBuy.setGroupId(baseGoodsGroup.getId());
        transBuy.setSellUserName(userName);
        transBuy.setGoodsName(baseGoodsGroup.getName());
        transBuy.setMoney(money);
        transBuy.setStatus(EnumUtil.TransBuyStatus.Selling.toString());
        transBuy.setType(EnumUtil.TransBuyType.Seller.toString());
        transBuy.setCreateDate(new Date());
        transBuy.setSellNum(num);
        transBuy.setNowNum(num);
        this.save(transBuy);
    }


    /**
     * 发布求购
     *
     * @param
     * @param money
     * @param num
     * @return
     */

    public void publishBuy(String userName, String groupId, BigDecimal money, int num) throws ValidationException {
        //判断价格是否合理
        canPublish(groupId,money);
        //判断是否可交易
        isTransGroup(groupId);

        //判断交易是否关闭
        UserUserinfo userinfo = userUserinfoService.getByNameLock(userName);
        BigDecimal numDecimal;
        try {
            numDecimal =BigDecimal.valueOf(num);
        } catch (Exception e) {
            throw new ValidationException("发布求购数量错误");
        }
        //判断商品存在
        BaseGoodsGroup baseGoodsGroup = baseGoodsGroupService.get(groupId);
        if (null == baseGoodsGroup) {
            throw new ValidationException("当前产品不存在,不可发布");
        }

        if (money.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("产品求购价格必须大于0");
        }

        if (num<0) {
            throw new ValidationException("时间求购数量必须为正整数");
        }
        //判断是否在交易时间内
        if(!validateIsTransTime()){
            throw new ValidationException("交易未开始或者不在交易时间内。");
        }
        //买家手续费-实际得到
        BigDecimal buyShouXu ;
        try {
            buyShouXu = new BigDecimal(Global.getOption("system_trans","buy_procedure_money"));
        } catch (Exception e) {
            throw new ValidationException("买家手续费配置错误");
        }
        BigDecimal allMoney = money.multiply(numDecimal).multiply(BigDecimal.ONE.add(buyShouXu)).setScale(2,BigDecimal.ROUND_HALF_UP);
        if (userinfo.getMoney().compareTo(allMoney) < 0) {
            throw new ValidationException("用户余额不足,共需金额:"+money.multiply(new BigDecimal(1).add(buyShouXu)).multiply(numDecimal).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
        }
        //扣除用户金钱
        BigDecimal useMoney = money.multiply(numDecimal).setScale(2,BigDecimal.ROUND_HALF_UP);
        BigDecimal shouXuMoney = useMoney.multiply(buyShouXu).setScale(2,BigDecimal.ROUND_HALF_UP);

        userUserinfoService.updateUserMoney(userName, useMoney.multiply(new BigDecimal(-1)), "求购产品成功", EnumUtil.MoneyChangeType.transBuy);
        userUserinfoService.updateUserMoney(userName,shouXuMoney.multiply(new BigDecimal(-1)),"求购产品手续费", EnumUtil.MoneyChangeType.COUNTER_FEE);
        //发布交易表信息
        TransBuy transBuy = new TransBuy();
        transBuy.setGroupId(baseGoodsGroup.getId());
        transBuy.setSellUserName(userName);
        transBuy.setGoodsName(baseGoodsGroup.getName());
        transBuy.setMoney(money);
        transBuy.setStatus(EnumUtil.TransBuyStatus.Selling.toString());
        transBuy.setType(EnumUtil.TransBuyType.buyer.toString());
        transBuy.setCreateDate(new Date());
        transBuy.setSellNum(num);
        transBuy.setNowNum(num);
        this.save(transBuy);
    }

    /**
     * 判断是否在交易时间内
     * @throws ValidationException
     */
    private boolean validateIsTransTime() throws ValidationException {
        //判断是否在交易时间内
        Date beginTime ;
        Date endTime;
        Date beginTime2 ;
        Date endTime2;
        try {
            beginTime = DateUtils2.formatStrTime(Global.getOption("system_trans", "trans_time_begin"));
            endTime = DateUtils2.formatStrTime(Global.getOption("system_trans", "trans_time_end"));

            beginTime2 = DateUtils2.formatStrTime(Global.getOption("system_trans", "trans_time_begin2"));
            endTime2 = DateUtils2.formatStrTime(Global.getOption("system_trans", "trans_time_end2"));
        } catch (ValidationException e) {
            throw new ValidationException("转让交易时间配置错误。");
        }
        Date nowtime = DateUtils2.formatStrTime(DateUtils2.getTime());
        if (!(nowtime.getTime() > beginTime.getTime() && nowtime.getTime() < endTime.getTime()) && !(nowtime.getTime() > beginTime2.getTime() && nowtime.getTime() < endTime2.getTime()) || !Global.isOptionOpen("system_trans", "trans_open")) {
            return false;
        }else {
            return true;
        }
    }


    /**
     * 购买
     *
     * @param
     * @param
     * @param num
     */

    public int promiseSell(String userName, String groupId, BigDecimal money, BigDecimal num) throws ValidationException {

        //判断是否可交易
        isTransGroup(groupId);

        UserUserinfo userinfo = userUserinfoService.getByNameLock(userName);
        //判断商品存在
        BaseGoodsGroup baseGoodsGroup  = baseGoodsGroupService.get(groupId);
        if (null == baseGoodsGroup) {
            throw new ValidationException("当前交易品不存在,不可发布");
        }
        if(userinfo == null){
            throw new ValidationException("用户不存在!");
        }
        if (money.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("价格必须大于0");
        }
        if (!VerifyUtils.isInteger(num)) {
            throw new ValidationException("数量必须为正整数");
        }
        //判断是否在交易时间内
        if(!validateIsTransTime()){
            throw new ValidationException("交易未开始或者不在交易时间内。");
        }
        BigDecimal allMoney = money.multiply(num).setScale(2,BigDecimal.ROUND_HALF_UP);
        BigDecimal sellShouXu ;
        BigDecimal buyShouXu ;
        try {
            //卖家手续费-实际得到
            sellShouXu = new BigDecimal(Global.getOption("system_trans","sell_procedure_money"));
            //买家手续费-实际得到
            buyShouXu = new BigDecimal(Global.getOption("system_trans","buy_procedure_money"));
        } catch (Exception e) {
            throw new ValidationException("手续费配置错误");
        }
        if (userinfo.getMoney().compareTo(allMoney.multiply(BigDecimal.valueOf(1).add(buyShouXu))) < 0) {
            throw new ValidationException("用户余额不足,共需金额:"+money.multiply(BigDecimal.valueOf(1).add(buyShouXu)).multiply(num).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
        }
        //查询应买单交易品
        TransBuy transBuy = new TransBuy();
        transBuy.setStatus(EnumUtil.TransBuyStatus.Selling.toString());
        transBuy.setType(EnumUtil.TransBuyType.Seller.toString());
        transBuy.setMoney(money);
        transBuy.setGroupId(baseGoodsGroup.getId());
        transBuy.setOrderBy("a.id asc");
        List<TransBuy> transBuyList = findListLock(transBuy);
        if (transBuyList.size() <= 0) {
            throw new ValidationException("来晚一步,该商品已经抢购空了.");
        }
        int buyNum = num.intValue();
        for (int i = 0; i < transBuyList.size(); i++) {
            TransBuy transBuyTemp = transBuyList.get(i);
            if (transBuyTemp.getNowNum() > 0) {
                //修改卖出人的商品状态
                int changeNum = transBuyTemp.getNowNum() - buyNum < 0 ? transBuyTemp.getNowNum()  :  buyNum;
                transBuyTemp.setNowNum(transBuyTemp.getNowNum() - changeNum);
                if (transBuyTemp.getNowNum() == 0) {
                    transBuyTemp.setStatus(EnumUtil.TransBuyStatus.Selled.toString());
                }
                transBuyTemp.preUpdate();
                dao.update(transBuyTemp);
                //给买入者增加商品
                transGoodsService.updateGoodsNum(userName, groupId, changeNum);

                //卖家实际收入
                BigDecimal activeMoney =  transBuyTemp.getMoney().multiply(BigDecimal.valueOf(changeNum)).setScale(2,BigDecimal.ROUND_HALF_UP);

                //修改卖家金钱
                userUserinfoService.updateUserMoney(transBuyTemp.getSellUserName(), activeMoney, "成功出售"+changeNum+"个产品", EnumUtil.MoneyChangeType.transSell);
                //扣除卖家手续费
                userUserinfoService.updateUserMoney(transBuyTemp.getSellUserName(), activeMoney.multiply(sellShouXu).multiply(BigDecimal.valueOf(-1).setScale(2,BigDecimal.ROUND_HALF_UP)), "扣除交易手续费,交易编号:"+transBuyTemp.getId(), EnumUtil.MoneyChangeType.COUNTER_FEE);

                //插入卖出账变
                TransBuyLog transBuyLog = new TransBuyLog();
                transBuyLog.setUserName(transBuyTemp.getSellUserName());
                transBuyLog.setTransUserName(userName);
                transBuyLog.setGroupId(transBuyTemp.getGroupId());
                transBuyLog.setGoodsName(transBuyTemp.getGoodsName());
                transBuyLog.setNum(changeNum);
                transBuyLog.setMoney(transBuyTemp.getMoney());
                transBuyLog.setMessage("出售成功");
                transBuyLog.setType(EnumUtil.TransBuyLogType.Sell.toString());
                transBuyLog.setBuyId(transBuyTemp.getId());
                transBuyLogService.save(transBuyLog);


                //修改买家金额
                userUserinfoService.updateUserMoney(userName, money.multiply(BigDecimal.valueOf(changeNum)).multiply(BigDecimal.valueOf(-1)).setScale(2,BigDecimal.ROUND_HALF_UP), "成功购买"+changeNum+"个产品", EnumUtil.MoneyChangeType.transBuy);
                //扣除买家手续费
                userUserinfoService.updateUserMoney(userName, money.multiply(BigDecimal.valueOf(changeNum)).multiply(buyShouXu).multiply(BigDecimal.valueOf(-1)).setScale(2,BigDecimal.ROUND_HALF_UP), "扣除交易手续费,交易编号:"+transBuyTemp.getId(), EnumUtil.MoneyChangeType.COUNTER_FEE);

                //插入买入账变
                transBuyLog = new TransBuyLog();
                transBuyLog.setUserName(userName);
                transBuyLog.setTransUserName(transBuyTemp.getSellUserName());
                transBuyLog.setGoodsName(transBuyTemp.getGoodsName());
                transBuyLog.setGroupId(transBuyTemp.getGroupId());
                transBuyLog.setNum(changeNum);
                transBuyLog.setMoney(transBuyTemp.getMoney().multiply(BigDecimal.valueOf(-1)));
                transBuyLog.setMessage("购买成功");
                transBuyLog.setType(EnumUtil.TransBuyLogType.buy.toString());
                transBuyLog.setBuyId(transBuyTemp.getId());
                transBuyLogService.save(transBuyLog);
                buyNum = buyNum - changeNum;
                if (buyNum <= 0) {
                    break;
                }
            }
        }
        return num.intValue() - buyNum;
    }



    //卖出
    public int promiseBuy(String userName, String groupId, BigDecimal money, BigDecimal num) throws ValidationException {

        //判断是否可交易
        isTransGroup(groupId);

        //判断商品存在
        userUserinfoService.getByNameLock(userName);
        BaseGoodsGroup baseGoodsGroup = baseGoodsGroupService.get(groupId);
        if (null == baseGoodsGroup) {
            throw new ValidationException("当前交易品不存在,不可发布");
        }
        if (money.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("价格必须大于0");
        }
        if (!VerifyUtils.isInteger(num)) {
            throw new ValidationException("数量必须为正整数");
        }

        //判断是否在交易时间内
        if(validateIsTransTime() == false){
            throw new ValidationException("交易未开始或者不在交易时间内。");
        }
        //判断用户交易品充足
        TransGoods transGoods = transGoodsService.getUserStar(groupId,userName);
        TransGoods transGoodsSell = transGoodsService.getLock(transGoods.getId());
        if(transGoodsSell.getNum()<num.intValue()){
            throw new ValidationException("当前持有时间不足。");
        }

        //查询应买单交易品
        TransBuy transBuy = new TransBuy();
        transBuy.setStatus(EnumUtil.TransBuyStatus.Selling.toString());
        transBuy.setType(EnumUtil.TransBuyType.buyer.toString());
        transBuy.setMoney(money);
        transBuy.setOrderBy("a.id asc");
        List<TransBuy> transBuyList = findListLock(transBuy);
        if (transBuyList.size() <= 0) {
            throw new ValidationException("来晚一步,本单需求已经全部求购完毕.");
        }
        int buyNum = num.intValue();

        for (int i = 0; i < transBuyList.size(); i++) {
            TransBuy transBuyTemp = transBuyList.get(i);
            if (transBuyTemp.getNowNum() > 0) {
                int changeNum = transBuyTemp.getNowNum() - buyNum < 0 ? transBuyTemp.getNowNum()  :  buyNum;
                transBuyTemp.setNowNum(transBuyTemp.getNowNum() - changeNum);
                if (transBuyTemp.getNowNum() == 0) {
                    transBuyTemp.setStatus(EnumUtil.TransBuyStatus.Selled.toString());
                }
                transBuyTemp.preUpdate();
                dao.update(transBuyTemp);
                //减少卖出者商品
                transGoodsService.updateNum(transGoodsSell.getId(),-changeNum);

                //给买入者增加商品
                TransGoods buyGoods = transGoodsService.getUserStar(groupId,transBuyTemp.getSellUserName());
                if(buyGoods == null) {
                    buyGoods = new TransGoods();
                    buyGoods.setUserName(transBuyTemp.getSellUserName());
                    buyGoods.setName(transBuyTemp.getGoodsName());
                    buyGoods.setStatus(EnumUtil.GoodsType.hold.toString());
                    buyGoods.setGroupId(groupId);
                }
                buyGoods.setNum(buyGoods.getNum()+changeNum);

                transGoodsService.save(buyGoods);

                //卖家手续费-实际得到
                BigDecimal sellShouXu = null;
                try {
                    sellShouXu = new BigDecimal(Global.getOption("system_trans","sell_procedure_money"));
                } catch (Exception e) {
                    throw new ValidationException("手续费配置错误");
                }

                //修改卖家金钱
                userUserinfoService.updateUserMoney(userName, transBuyTemp.getMoney().multiply(BigDecimal.valueOf(changeNum)), "产品出售成功", EnumUtil.MoneyChangeType.transSell);
                //扣除卖家手续费
                userUserinfoService.updateUserMoney(userName, transBuyTemp.getMoney().multiply(BigDecimal.valueOf(changeNum)).multiply(sellShouXu).multiply(BigDecimal.valueOf(-1)), "扣除交易手续费,交易编号:"+transBuyTemp.getId(), EnumUtil.MoneyChangeType.COUNTER_FEE);

                //插入卖出账变
                TransBuyLog transBuyLog = new TransBuyLog();
                transBuyLog.setUserName(userName);
                transBuyLog.setTransUserName(transBuyTemp.getSellUserName());
                transBuyLog.setGroupId(transBuyTemp.getGroupId());
                transBuyLog.setNum(changeNum);
                transBuyLog.setGoodsName(transBuyTemp.getGoodsName());
                transBuyLog.setMoney(transBuyTemp.getMoney());
                transBuyLog.setMessage("时间出售成功");
                transBuyLog.setType(EnumUtil.TransBuyLogType.Sell.toString());
                transBuyLog.setBuyId(transBuyTemp.getId());
                transBuyLogService.save(transBuyLog);

                //插入买入账变
                transBuyLog = new TransBuyLog();
                transBuyLog.setUserName(transBuyTemp.getSellUserName());
                transBuyLog.setTransUserName(userName);
                transBuyLog.setGroupId(transBuyTemp.getGroupId());
                transBuyLog.setNum(changeNum);
                transBuyLog.setGoodsName(transBuyTemp.getGoodsName());
                transBuyLog.setMoney(transBuyTemp.getMoney().multiply(BigDecimal.valueOf(-1)));
                transBuyLog.setMessage("时间购买成功");
                transBuyLog.setType(EnumUtil.TransBuyLogType.buy.toString());
                transBuyLog.setBuyId(transBuyTemp.getId());
                transBuyLogService.save(transBuyLog);
                buyNum = buyNum - changeNum;
                if (buyNum <= 0) {
                    break;
                }
            }
        }
        return num.intValue() - buyNum;
    }



    /**
     * 交易品下架
     *
     * @param userName      用户编号
     * @param buyStatusEnum 下架方式,  自动 还是 手动
     */

    public void promiseCancel(String userName, String buyId, EnumUtil.TransBuyStatus buyStatusEnum, String downType) throws ValidationException {

        //查询交易发布
        TransBuy transBuy =  this.get(buyId);
        if(transBuy == null){
            throw new ValidationException("所要下架的场次不存在");
        }
        if(!userName.equals(transBuy.getSellUserName())){
            throw new ValidationException("只能下架自己的订单");
        }
        transBuy.setStatus(buyStatusEnum.toString());
        transBuy.setDownNum(transBuy.getNowNum());
        transBuy.setNowNum(0);
        this.save(transBuy);
        //买家手续费-实际得到
        BigDecimal buyShouXu ;
        try {
            buyShouXu = new BigDecimal(Global.getOption("system_trans","buy_procedure_money"));
        } catch (Exception e) {
            throw new ValidationException("手续费配置错误");
        }
        if(transBuy.getType().equals(EnumUtil.TransBuyType.Seller.toString())){
            // 修改出售的商品状态
            TransGoods transGoods = transGoodsService.getUserStar(transBuy.getGroupId(),userName);
            transGoodsService.updateNum(transGoods.getId(),transBuy.getDownNum());

        }
        else if(transBuy.getType().equals(EnumUtil.TransBuyType.buyer.toString())){
            userUserinfoService.updateUserMoney(userName, transBuy.getMoney().multiply(new BigDecimal(transBuy.getDownNum())).multiply(new BigDecimal(1).add(buyShouXu)).setScale(2,BigDecimal.ROUND_HALF_DOWN), downType+"数量"+transBuy.getDownNum()+",交易编号:" + transBuy.getId() + " 产品编号:" + transBuy.getGroupId(), EnumUtil.MoneyChangeType.transDownUser);
        }
    }

    /**
     * 兑换积分
     * @param exchangeNum
     * @param
     * @throws ValidationException
     */
    public int exchangeScore(String goodsId,String groupId,String userName,int exchangeNum,String message) throws ValidationException {
        BigDecimal attornScale;
        TransGoods transGoods = transGoodsService.get(goodsId);
        //判断是否可交易
        isTransGroup(groupId);
        int realNum;
        if(transGoods == null){
            throw new ValidationException("所要兑换的产品不存在");
        }
        if(!transGoods.getUserName().equals(userName)){
            throw new ValidationException("只能兑换自己的产品");
        }
        try {
            attornScale = saleByAuctionService.getByGroupId(transGoods.getGroupId()).getMoney().multiply(new BigDecimal(Global.getOption("system_trans","attorn_scale")));
        } catch (Exception e) {
            throw new ValidationException("兑换比例错误!");
        }
        realNum = transGoods.getNum()<exchangeNum?transGoods.getNum():exchangeNum;
        if(realNum == 0){
            throw new ValidationException("积分不足,无法兑换");
        }
        userUserinfoService.updateUserScore(userName,attornScale.multiply(BigDecimal.valueOf(realNum)),message+realNum+"秒兑换积分,产品编号："+transGoods.getGroupId(),EnumUtil.MoneyChangeType.SCORE_ATTORN);

        TransBuyLog transBuyLog = new TransBuyLog();
        transBuyLog.setGroupId(transGoods.getGroupId());
        transBuyLog.setGoodsName(transGoods.getName());
        transBuyLog.setUserName(userName);
        transBuyLog.setTransUserName("平台");
        transBuyLog.setTransUserTrueName("平台");
        transBuyLog.setMoney(BigDecimal.ONE);
        transBuyLog.setNum(realNum);
        transBuyLog.setMessage("兑换积分");
        transBuyLog.setType(EnumUtil.TransBuyLogType.apply.toString());
        transBuyLogService.save(transBuyLog);

        //  扣除对应明星的时间。
        transGoodsService.updateNum(transGoods.getId(),-realNum);

        return realNum;
    }

    /**
     * 兑换明星产品
     * @param id
     * @throws ValidationException
     */
    public void exchangeProject(String goodsId,String groupId,String id,String userName) throws ValidationException {

        //判断是否可交易
        isTransGroup(groupId);
        UserUserinfo userinfo = userUserinfoService.getByName(userName);
        StarProject starProject = starProjectService.get(id);
        if(starProject == null){
            throw new ValidationException("兑换明星产品不存在。");
        }
        if(userinfo == null){
            throw new ValidationException("用户不存在。");
        }
        // 扣除用户对应明星时间
        TransGoods transGoods = transGoodsService.getLock(goodsId);
        if(transGoods.getNum()-starProject.getTime()<0){
            throw new ValidationException("您持有的时间不足，兑换失败。");
        }
        if(!transGoods.getUserName().equals(userName)){
            throw new ValidationException("只可以兑换自己的时间");
        }
        //兑换表插入记录
        TransOrder transOrder = new TransOrder();
        transOrder.setUserName(userinfo.getUserName());
        transOrder.setConsignee(userinfo.getTrueName());
        transOrder.setGroupId(starProject.getGroupId());
        transOrder.setMobile(userinfo.getMobile());
        transOrder.setNum(starProject.getTime());
        transOrder.setAddress(starProject.getImage());
        transOrder.setGoodsName(starProject.getName());
        transOrder.setType (EnumUtil.CheckType.uncheck.toString());
        transOrderService.save(transOrder);
        transGoods.setNum(transGoods.getNum()-starProject.getTime());
        transGoods.setStatus(EnumUtil.GoodsType.hold.toString());
        transGoodsService.save(transGoods);

    }

    /**
     * 判断是否可交易
     * @param groupId
     */
    public void isTransGroup(String groupId) {
        BaseGoodsGroup goodsGroup = baseGoodsGroupService.get(groupId);
        if(goodsGroup == null){
            throw new ValidationException("明星不存在");
        }
        if(!GoodsStatus.ATTORN.toString().equals(goodsGroup.getStatus())){
            throw new ValidationException("未到交易期,请等待");
        }
    }

    /**
     * 判断价格是否合理
     * @param groupId
     * @return
     */
    public boolean canPublish(String groupId,BigDecimal money){
        BigDecimal highPrice; //最高价
        try {
            highPrice = mdTradeMainService.getNowMoney(groupId).getTransactionPrice().multiply(BigDecimal.ONE.add(new BigDecimal(Global.getOption("system_trans","attorn_price")))).setScale(2,BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            throw new ValidationException("转让价格比例配置错误");
        }
        if(money.compareTo(highPrice)>0){
            throw new ValidationException("价格过高,最高价为"+highPrice);
        }
        return true;
    }

}