package main.qysoft.md.service;

import com.google.common.collect.Lists;
import main.Main;
import main.qysoft.md.dao.MdTradeRepository;
import main.qysoft.md.entity.*;
import main.qysoft.md.entity.akkadto.MdTradeDTO;
import main.qysoft.md.entity.akkadto.TransResultDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by kevin on 2017/11/2.
 */
@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackForClassName = {"RuntimeException", "Exception"})
public class MdTradeService {
    @Autowired
    MdTradeRepository mdTradeRepository;

    @Autowired
    UserUserInfoService userUserInfoService;

    @Autowired
    MdTradeLogService mdTradeLogService;

    @Autowired
    TransGoodsService transGoodsService;

    @Autowired
    RealTimePriceService realTimePriceService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public List<MdTrade> findLockByIds(Long id1, Long id2) {
        if (id1 == null || id2 == null) {
            return Lists.newArrayList();
        }

        return mdTradeRepository.findLockByIds(id1, id2);
    }

    /**
     * 获取最新发布的交易
     *
     * @param lastId
     * @return
     */
    public List<MdTrade> getNewPublishedTrans(String groupId, long lastId) {
        return mdTradeRepository.getNewPublishedTrans(groupId, lastId);
    }

    /**
     * 用户撤单
     *
     * @param trade
     */
    public void userCancelTrade(MdTrade trade) {
        if (null == trade) {
            return;
        }

        Optional<UserUserinfo> user = userUserInfoService.findLockById(trade.getUserId());
        if (!user.isPresent()) {
            return;
        }

        MdTrade tradeFromDB = mdTradeRepository.findLockById(trade.getId());
        if (TradeState.FINISH.orginal() == tradeFromDB.getState()) {
            return;
        }

        if (tradeFromDB.getRemainNum() == 0) {
            tradeFromDB.setState(TradeState.FINISH.orginal());
            mdTradeRepository.save(tradeFromDB);
            return;
        }

        if (TradeType.BUY_IN.orignal() == trade.getType()) {    //如果是买，返还金额，清空数量
            BigDecimal bond = trade.getBond();
            BigDecimal profit = trade.getProfit();

            //boolean userCenter = userUserInfoService.isUserCenter(user.get(), trade.getGroupId());
            boolean userCenter = "2".equals(trade.getRemarks()) ? true : false;     //撤单根据remark字段判断退还金额至Money或monye2
            userUserInfoService.updateUserMoney(user.get().getId(), bond.add(profit), userCenter, ChangeType.USER_CANCEL_REFUND.orignal(), createCancelMsg(trade));

            trade.setBond(BigDecimal.ZERO);
            trade.setProfit(BigDecimal.ZERO);
        } else {    // 如果是卖，清空数量，返还持仓
            updateUserPosition(trade, trade.getRemainNum());
        }

        if (trade.getRemainNum() <= 0) {    // 如果剩余数量为0，说明已经撮合成功
            trade.setState(TradeState.FINISH.orginal());
        } else {
            trade.setState(TradeState.CANCEL_SUCCESS.orginal());
        }
        trade.setRemainNum(0);
        mdTradeRepository.save(trade);
    }

    private String createCancelMsg(MdTrade trade) {
        StringBuilder result = new StringBuilder();
        result.append("用户撤单，退回交易金额及手续费");
        result.append("【单号:").append(trade.getId()).append("】");
        result.append("【撤单数量:").append(trade.getRemainNum()).append("】");
        result.append("【发布数量:").append(trade.getPublishNum()).append("】");
        return result.toString();
    }

    /**
     * 保存交易
     * @param trade
     */
    public void save(MdTrade trade) {
        if (null == trade) {
            return;
        }

        mdTradeRepository.save(trade);
    }

    /**
     * 批量保存交易
     * @param trades
     */
    public void saveAll(Iterable<MdTrade> trades) {
        if (null == trades) {
            return;
        }
        mdTradeRepository.save(trades);
    }

    /**
     * 处理撮合结果
     * @param result
     */
    public void handleMdTradeResult(TransResultDTO result) throws Exception {
        if (result == null) {
            return;
        }

        BigDecimal currentTransPrice = result.getCurrentTransPrice();
        long transNum = result.getTransNum();
        MdTradeDTO tradeInQueueDTO = result.getTradeInQueue();
        MdTradeDTO newPublishedTradeDTO = result.getNewPublishedTrade();

        List<MdTrade> tradeByIds = findLockByIds(newPublishedTradeDTO.getId(), tradeInQueueDTO.getId());
        if (CollectionUtils.isEmpty(tradeByIds) || tradeByIds.size() != 2) {
            logger.error("未找到交易信息");
            return;
        }

        MdTrade newPublishTrade;
        MdTrade tradeInQueue;
        if (tradeByIds.get(0).getId().equals(newPublishedTradeDTO.getId())) {
            newPublishTrade = tradeByIds.get(0);
            tradeInQueue = tradeByIds.get(1);
        } else {
            newPublishTrade = tradeByIds.get(1);
            tradeInQueue = tradeByIds.get(0);
        }
        newPublishTrade.setUpdateDate(newPublishedTradeDTO.getUpdateDate());
        tradeInQueue.setUpdateDate(tradeInQueueDTO.getUpdateDate());

        MdTrade buyerTradeInfo;
        MdTrade sellerTradeInfo;
        if (TradeType.BUY_IN.orignal() == newPublishTrade.getType()) {
            buyerTradeInfo = newPublishTrade;
            sellerTradeInfo = tradeInQueue;
        } else {
            sellerTradeInfo = newPublishTrade;
            buyerTradeInfo = tradeInQueue;
        }

        if (buyerTradeInfo.getBond().compareTo(currentTransPrice.multiply(BigDecimal.valueOf(transNum)).setScale(2, BigDecimal.ROUND_DOWN)) < 0) {
            StringBuilder errInfo = new StringBuilder();
            errInfo.append("买方余额不足，无法完成撮合。【Buy_ID:").append(buyerTradeInfo.getId()).append("】");
            errInfo.append("【Sell_ID:").append(sellerTradeInfo.getId()).append("】");
            errInfo.append("【余额: ").append(buyerTradeInfo.getBond()).append("】");
            errInfo.append("【实际金额: ").append(currentTransPrice.multiply(BigDecimal.valueOf(transNum)).setScale(2, BigDecimal.ROUND_DOWN)).append("】");
            logger.error(errInfo.toString());
            return;
        }

        if (buyerTradeInfo.getRemainNum() < transNum) {
            StringBuilder errInfo = new StringBuilder();
            errInfo.append("买方剩余量不足，无法完成撮合。【Buy_ID:").append(buyerTradeInfo.getId()).append("】");
            errInfo.append("【Sell_ID:").append(sellerTradeInfo.getId()).append("】");
            errInfo.append("【剩余数量: ").append(buyerTradeInfo.getRemainNum()).append("】");
            errInfo.append("【实际交易数量: ").append(transNum).append("】");
            logger.error(errInfo.toString());
            return;
        }

        if (sellerTradeInfo.getRemainNum() < transNum) {
            StringBuilder errInfo = new StringBuilder();
            errInfo.append("卖方剩余量不足，无法完成撮合。【Buy_ID:").append(buyerTradeInfo.getId()).append("】");
            errInfo.append("【Sell_ID:").append(sellerTradeInfo.getId()).append("】");
            errInfo.append("【剩余数量: ").append(sellerTradeInfo.getRemainNum()).append("】");
            errInfo.append("【实际交易数量: ").append(transNum).append("】");
            logger.error(errInfo.toString());
            return;
        }

        // 更新交易状态
        modifyTradeInfo(currentTransPrice, transNum, buyerTradeInfo, sellerTradeInfo);
        // 插入交易日志
        createTransLog(buyerTradeInfo, sellerTradeInfo, transNum, currentTransPrice, result.getTimeStamp());

        // 更新卖方余额
        // 如果是承销商，修改Money2，否则修改Money
        updateSellerMoney(transNum, buyerTradeInfo, sellerTradeInfo, currentTransPrice);

        // 修改买家持仓
        updateUserPosition(buyerTradeInfo, transNum);

        // 插入分时图数据
        insertRealTimeChartInfo(result);
    }

    private void insertRealTimeChartInfo(TransResultDTO result) {
        RealTimePrice realTimePrice = new RealTimePrice();
        MdTradeDTO newPublishedTrade1 = result.getNewPublishedTrade();

        realTimePrice.setCreateDate(newPublishedTrade1.getUpdateDate());
        realTimePrice.setGroupId(result.getNewPublishedTrade().getGroupId());
        realTimePrice.setPrice(result.getCurrentTransPrice());
        realTimePrice.setTimeStamp(result.getTimeStamp());

        realTimePriceService.save(realTimePrice);
    }

    /**
     * 修改买家持仓
     * @param tradeInfo
     * @param transNum
     */
    private void updateUserPosition(MdTrade tradeInfo, long transNum) {
        TransGoods transGoods;
        Optional<TransGoods> transGoodsInfoOptional = transGoodsService.findLockByUserNameAndGroupId(tradeInfo.getUserName(), tradeInfo.getGroupId());
        if (transGoodsInfoOptional.isPresent()) {
            transGoods = transGoodsInfoOptional.get();
        } else {
            transGoods = new TransGoods();
            transGoods.setGroupId(tradeInfo.getGroupId());
            transGoods.setUserName(tradeInfo.getUserName());
            transGoods.setCreateDate(new Date());
        }

        transGoods.setUpdateDate(new Date());
        transGoods.setStatus("2");
        transGoods.setNum(transGoods.getNum() + transNum);

        transGoodsService.save(transGoods);
    }

    /**
     * 更新卖方余额
     * @param transNum
     * @param sellerTradeInfo
     * @param currTransPrice
     */
    private void updateSellerMoney(long transNum, MdTrade buyerTradeInfo, MdTrade sellerTradeInfo, BigDecimal currTransPrice) throws Exception {
        Optional<UserUserinfo> userInfo = userUserInfoService.findLockById(sellerTradeInfo.getUserId());
        if (!userInfo.isPresent()) {
            throw new Exception("获取卖方信息失败");
        }

        // 计算实际交易价格（扣除手续费后）
        BigDecimal tradeFee = sellerTradeInfo.getFee();
        BigDecimal fee = BigDecimal.ONE.subtract(tradeFee);
        if (fee.compareTo(BigDecimal.ZERO) <= 0) {
            fee = BigDecimal.ONE;
        }
        BigDecimal transPrice = currTransPrice.multiply(BigDecimal.valueOf(transNum)).multiply(fee).setScale(2, BigDecimal.ROUND_DOWN);
        boolean userCenter = isUserCenter(userInfo.get(), sellerTradeInfo);

        BigDecimal feePrice = currTransPrice.multiply(tradeFee).multiply(BigDecimal.valueOf(transNum));
        userUserInfoService.updateUserMoney(userInfo.get().getId(), transPrice, userCenter, ChangeType.MD_SUCCESS_ADD_SELLER_MONEY.orignal(), createMdSuccMessage(feePrice, transPrice, buyerTradeInfo.getId(), sellerTradeInfo.getId()));
    }

    /**
     * 生成撮合成功日志信息
     * @param transPrice
     */
    private String createMdSuccMessage(BigDecimal feePrice, BigDecimal transPrice, Long buyerTradeId, Long sellerTradeId) {
        StringBuilder msg = new StringBuilder();
        msg.append("撮合成功，获得交易金额:【").append(transPrice.toString()).append("】");
        msg.append(" 手续费:【").append(feePrice.toString()).append("】");
        msg.append(" 买交易:【").append(buyerTradeId.toString()).append("】");
        msg.append(" 卖交易:【").append(sellerTradeId.toString()).append("】");

        return msg.toString();
    }

    /**
     * 生成交易日志
     *
     * @param buyerTradeInfo
     * @param sellerTradeInfo
     * @param transNum
     * @param currTransPrice
     */
    private void createTransLog(MdTrade buyerTradeInfo, MdTrade sellerTradeInfo, long transNum, BigDecimal currTransPrice, long timeStamp) throws Exception {
        // 插入交易日志 买
        MdTradeLog mdTradeLogBuy = new MdTradeLog();
        mdTradeLogBuy.setAmount(transNum);
        mdTradeLogBuy.setGroupId(buyerTradeInfo.getGroupId());
        mdTradeLogBuy.setPrice(currTransPrice);
        mdTradeLogBuy.setType(buyerTradeInfo.getType());
        mdTradeLogBuy.setCreateDate(buyerTradeInfo.getUpdateDate());

        // 计算买手续费
        BigDecimal feeOfBuy = currTransPrice.multiply(buyerTradeInfo.getFee()).multiply(BigDecimal.valueOf(transNum)).setScale(2, BigDecimal.ROUND_DOWN);
        mdTradeLogBuy.setProfit(feeOfBuy);

        mdTradeLogBuy.setUserName(buyerTradeInfo.getUserName());
        mdTradeLogBuy.setUserId(buyerTradeInfo.getUserId());
        mdTradeLogBuy.setMessage("交易撮合成功。买交易：【" + buyerTradeInfo.getId() + "】 卖交易：" + sellerTradeInfo.getId() + "】");
        mdTradeLogBuy.setDelFlag("0");
        mdTradeLogBuy.setMdTradeId(buyerTradeInfo.getId());
        mdTradeLogBuy.setRemarks("成交价：【" + currTransPrice + "】 成交量：【" + transNum + "】");
        mdTradeLogBuy.setUpdateDate(buyerTradeInfo.getUpdateDate());
        mdTradeLogBuy.setTimeStamp(timeStamp);
        mdTradeLogService.save(mdTradeLogBuy);

        // 插入交易日志 卖
        MdTradeLog mdTradeLogSell = new MdTradeLog();
        mdTradeLogSell.setAmount(transNum);
        mdTradeLogSell.setGroupId(sellerTradeInfo.getGroupId());
        mdTradeLogSell.setPrice(currTransPrice);
        mdTradeLogSell.setType(sellerTradeInfo.getType());
        mdTradeLogSell.setCreateDate(sellerTradeInfo.getUpdateDate());

        // 计算手续费
        BigDecimal feeOfSell = currTransPrice.multiply(sellerTradeInfo.getFee()).multiply(BigDecimal.valueOf(transNum)).setScale(2, BigDecimal.ROUND_DOWN);
        mdTradeLogSell.setProfit(feeOfSell);

        mdTradeLogSell.setUserName(sellerTradeInfo.getUserName());
        mdTradeLogSell.setUserId(sellerTradeInfo.getUserId());
        mdTradeLogSell.setMessage("交易撮合成功。买交易：【" + buyerTradeInfo.getId() + "】 卖交易：" + sellerTradeInfo.getId() + "】");
        mdTradeLogSell.setDelFlag("0");
        mdTradeLogSell.setMdTradeId(sellerTradeInfo.getId());
        mdTradeLogSell.setRemarks("成交价：【" + currTransPrice + "】 成交量：【" + transNum + "】");
        mdTradeLogSell.setUpdateDate(sellerTradeInfo.getUpdateDate());
        mdTradeLogSell.setTimeStamp(timeStamp);
        mdTradeLogService.save(mdTradeLogSell);
    }

    /**
     * 是否是承销商
     *
     * @param userUserInfo
     * @param sellerTradeInfo
     * @return
     */
    private boolean isUserCenter(UserUserinfo userUserInfo, MdTrade sellerTradeInfo) {
        if (userUserInfo.getIsUserCenter() == UserUserinfo.IS_USER_CENTER && sellerTradeInfo.getGroupId().equals(userUserInfo.getUserCenterAddress())) {
            return true;
        }
        return false;
    }

    /**
     * 更新交易状态
     * @param currentTransPrice
     * @param transNum
     * @param buyerTradeInfo
     * @param sellerTradeInfo
     */
    private void modifyTradeInfo(BigDecimal currentTransPrice, long transNum, MdTrade buyerTradeInfo, MdTrade sellerTradeInfo) {
        sellerTradeInfo.setRemainNum(sellerTradeInfo.getRemainNum() - transNum);
        buyerTradeInfo.setRemainNum(buyerTradeInfo.getRemainNum() - transNum);

        // 扣除买方手续费
        BigDecimal profit = buyerTradeInfo.getProfit();
        BigDecimal shuoldPay = currentTransPrice.multiply(buyerTradeInfo.getFee()).multiply(BigDecimal.valueOf(transNum)).setScale(2, BigDecimal.ROUND_DOWN);
        if (profit.compareTo(shuoldPay) <= 0) {
            buyerTradeInfo.setProfit(BigDecimal.ZERO);
        } else {
            buyerTradeInfo.setProfit(profit.subtract(shuoldPay));
        }

        // 扣除买方交易费用
        BigDecimal bond = buyerTradeInfo.getBond();
        BigDecimal shouldPayBond = currentTransPrice.multiply(BigDecimal.valueOf(transNum)).setScale(2, BigDecimal.ROUND_DOWN);
        if (bond.compareTo(shouldPayBond) <= 0) {
            buyerTradeInfo.setBond(BigDecimal.ZERO);
        } else {
            buyerTradeInfo.setBond(bond.subtract(shouldPayBond));
        }

        // 修改交易状态
        if (buyerTradeInfo.getRemainNum() <= 0) {
            buyerTradeInfo.setState(TradeState.FINISH.orginal());
            buyerTradeInfo.setLastState(LastTransState.NORMAL.orginal());
        } else {
            if (TradeState.USER_CANCEL.orginal() != buyerTradeInfo.getState()) {
                buyerTradeInfo.setState(TradeState.ON_TRADING.orginal());
            }
            buyerTradeInfo.setLastState(LastTransState.UNFINISHED.orginal());
        }

        if (sellerTradeInfo.getRemainNum() <= 0) {
            sellerTradeInfo.setState(TradeState.FINISH.orginal());
            sellerTradeInfo.setLastState(LastTransState.NORMAL.orginal());
        } else {
            if (TradeState.USER_CANCEL.orginal() != sellerTradeInfo.getState()) {
                sellerTradeInfo.setState(TradeState.ON_TRADING.orginal());
            }
            sellerTradeInfo.setLastState(LastTransState.UNFINISHED.orginal());
        }

        /*buyerTradeInfo.setUpdateDate(new Date());
        sellerTradeInfo.setUpdateDate(new Date());*/

        saveAll(Arrays.asList(new MdTrade[]{buyerTradeInfo, sellerTradeInfo}));
    }

    /**
     * 返回已发布的所有商品ID
     * @return
     */
    public List<Integer> findPublishedGroupIds() {
        return mdTradeRepository.findPublishedGroupIds();
    }
}
