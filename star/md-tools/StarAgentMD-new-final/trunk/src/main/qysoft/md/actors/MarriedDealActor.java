package main.qysoft.md.actors;

import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.common.collect.Lists;
import main.Main;
import main.qysoft.md.entity.LastTransState;
import main.qysoft.md.entity.MdTrade;
import main.qysoft.md.entity.TradeState;
import main.qysoft.md.entity.TradeType;
import main.qysoft.md.entity.akkadto.MdTradeDTO;
import main.qysoft.md.entity.akkadto.TransResultDTO;
import main.qysoft.md.manage.CurrentDayTransManager;
import main.qysoft.md.manage.TransactionQueueManager;
import main.qysoft.md.service.MdTradeService;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

/**
 * 完成撮合业务的Actor
 * Created by kevin on 2017/10/21.
 */
public class MarriedDealActor extends UntypedAbstractActor {
    protected final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    TransactionQueueManager transQueueManager = Main.applicationContext.getBean(TransactionQueueManager.class);

    CurrentDayTransManager currentDayTransManager = Main.applicationContext.getBean(CurrentDayTransManager.class);

    MdTradeService mdTradeService = Main.applicationContext.getBean(MdTradeService.class);

    @Override
    public void onReceive(Object msg) throws Throwable {
        if (msg instanceof String) {
            String groupId = (String) msg;
            List<MdTrade> newPublishedTrans = mdTradeService.getNewPublishedTrans(groupId, transQueueManager.getLastQueryTransId(groupId));
            if (CollectionUtils.isEmpty(newPublishedTrans)) {
                getSender().tell(ActorMsg.CLOSE, getSelf());
                getContext().stop(getSelf());
                return;
            }

            //更新查询最大ID
            Optional<MdTrade> maxTrade = newPublishedTrans.parallelStream().max(comparing(MdTrade::getId));
            if (maxTrade.isPresent()) {
                transQueueManager.setLastTransId(maxTrade.get().getGroupId(), maxTrade.get().getId());
            }

            doCancelTrade(newPublishedTrans);
            pushTradsIntoQueue(groupId, newPublishedTrans);

            //TradeType tradeType = TradeType.BUY_IN;
            TradeType tradeType;
            Optional<TradeType> tradeTypeOptional = transQueueManager.getTradeType(groupId);
            if (tradeTypeOptional.isPresent()) {
                tradeType = tradeTypeOptional.get();
            } else {
                tradeType = TradeType.BUY_IN;
            }

            ReentrantLock lock = transQueueManager.getLock(groupId);
            lock.lock();
            try {
                List<MdTradeDTO> unfinishedTrade = Lists.newLinkedList();
                while (!transQueueManager.queueIsEmpty(groupId)) {
                    LinkedList<TransResultDTO> result = Lists.newLinkedList();

                    MdTradeDTO mdTradeDTO = null;
                    if (TradeType.BUY_IN == tradeType) {
                        Optional<MdTradeDTO> mdTradeDTOOptional = transQueueManager.popBuyIn(groupId);
                        if (mdTradeDTOOptional.isPresent()) {
                            mdTradeDTO = mdTradeDTOOptional.get();
                        }
                    } else {
                        Optional<MdTradeDTO> mdTradeDTOOptional = transQueueManager.popSellOut(groupId);
                        if (mdTradeDTOOptional.isPresent()) {
                            mdTradeDTO = mdTradeDTOOptional.get();
                        }
                    }

                    if (null == mdTradeDTO) {
                        continue;
                    }
                    MdTradeDTO resultTradeDTO = marriedDeal2(mdTradeDTO, result);
                    if (resultTradeDTO.getRemainNum() > 0) {
                        unfinishedTrade.add(resultTradeDTO);
                    }

                    // 给结果处理actor发送消息
                    if (CollectionUtils.isNotEmpty(result)) {
                        getSender().tell(Collections.unmodifiableList(result), getSelf());
                    }
                    //tradeType = TradeType.BUY_IN == tradeType ? TradeType.SELL_OUT : TradeType.BUY_IN;
                }

                // 将为撮合完的交易放回待撮合队列
                if (!CollectionUtils.isEmpty(unfinishedTrade)) {
                    transQueueManager.pushTradeInQueue(unfinishedTrade);
                }

                if (TradeType.BUY_IN == tradeType) {
                    transQueueManager.setTradeType(groupId, TradeType.SELL_OUT);
                } else {
                    transQueueManager.setTradeType(groupId, TradeType.BUY_IN);
                }
                getSender().tell(ActorMsg.TRANS_RESULT_SUCCESS, getSelf());
            } finally {
                lock.unlock();
            }
        } else if (msg == ActorMsg.TRANS_RESULT_SUCCESS) {
            getContext().stop(getSender());
        } else {
            unhandled(msg);
        }
    }

    /**
     * 将交易放入待撮合队列
     *
     * @param groupId
     * @param tradeList
     */
    private void pushTradsIntoQueue(String groupId, List tradeList) {
        tradeList.forEach(obj -> {
            MdTrade trade = (MdTrade) obj;
            if (!transQueueManager.tradeIsOnTrading(trade.getGroupId(), trade.getId())) {
                if (TradeType.BUY_IN.orignal() == trade.getType()) {
                    transQueueManager.pushBuyIn(groupId, new MdTradeDTO(trade));
                } else {
                    transQueueManager.pushSellOut(groupId, new MdTradeDTO(trade));
                }
            }
        });
    }

    /**
     * 如果有用户撤单，执行撤单业务
     *
     * @param tradeList
     */
    private void doCancelTrade(List tradeList) {
        List<MdTrade> canceledTrade = (List<MdTrade>) tradeList.stream()
                .filter(trade -> ((MdTrade) trade).getState() == TradeState.USER_CANCEL.orginal()).collect(toList());
        if (!CollectionUtils.isEmpty(canceledTrade)) {
            List<MdTradeDTO> canceledTradeDTO = canceledTrade.parallelStream().map(trade -> new MdTradeDTO(trade)).collect(toList());
            // 执行撤单业务
            try {
                canceledTrade.forEach(beCanceledTrade -> mdTradeService.userCancelTrade(beCanceledTrade));
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }

            tradeList.removeAll(canceledTrade);
            transQueueManager.removeTrades(canceledTradeDTO);
        }
    }

    /**
     * 撮合交易
     *
     * @param trade
     */
    private MdTradeDTO marriedDeal2(MdTradeDTO trade, LinkedList<TransResultDTO> transResult) {
        // 循环从队列读取交易，直至目标队列没有合适交易可以撮合，将取出交易放回
        boolean isFinish = false;
        List<MdTradeDTO> tmpQueue = Lists.newLinkedList();
        MdTradeDTO mdTrade = trade.copy();
        String groupId = mdTrade.getGroupId();
        do {
            TransResultDTO mdResult;
            Optional<MdTradeDTO> mdTradeInQueueOptional;
            // 根据发布交易类型从队列中获取进行撮合的交易
            if (TradeType.BUY_IN.orignal() == mdTrade.getType()) {
                mdTradeInQueueOptional = transQueueManager.popSellOut(mdTrade.getGroupId());
            } else {
                mdTradeInQueueOptional = transQueueManager.popBuyIn(mdTrade.getGroupId());
            }

            if (mdTradeInQueueOptional.isPresent()) {
                MdTradeDTO mdTradeInQueue = mdTradeInQueueOptional.get();
                if (priceIsNotMatch(mdTrade, mdTradeInQueue) || mdTradeInQueue.getRemainNum() <= 0) { //价格不符合要求，撮合失败
                    transQueueManager.pushTradeInQueue(mdTradeInQueue.getGroupId(), mdTradeInQueue);    //将交易放回队列
                    break;
                } else {    //开始撮合
                    try {
                        MdTradeDTO buyerDTO;
                        MdTradeDTO sellerDTO;
                        if (TradeType.BUY_IN.orignal() == mdTrade.getType()) {
                            buyerDTO = mdTrade;
                            sellerDTO = mdTradeInQueue;
                        } else {
                            buyerDTO = mdTradeInQueue;
                            sellerDTO = mdTrade;
                        }

                        // 计算成交价
                        Optional<BigDecimal> currentTransPrice = currentDayTransManager.calculateTransPrice(buyerDTO, sellerDTO);
                        if (!currentTransPrice.isPresent()) {
                            log.error("计算成交价失败");
                            //transQueueManager.pushTradeInQueue(mdTradeInQueue.getGroupId(), mdTradeInQueue);
                            tmpQueue.add(mdTradeInQueue);
                            continue;
                        }

                        // 计算交易数量
                        long transNum = 0;
                        if (buyerDTO.getRemainNum() <= sellerDTO.getRemainNum()) {
                            transNum = buyerDTO.getRemainNum();
                        } else {
                            transNum = sellerDTO.getRemainNum();
                        }

                        // 计算剩余量是否满足交易条件
                        if (remainMoneyNotEnough(buyerDTO, sellerDTO, currentTransPrice, transNum)) {
                            //transQueueManager.pushTradeInQueue(mdTradeInQueue.getGroupId(), mdTradeInQueue);
                            tmpQueue.add(mdTradeInQueue);
                            continue;
                        }

                        if (remainAmountNotEnough(buyerDTO, sellerDTO, transNum)) {
                            //transQueueManager.pushTradeInQueue(mdTradeInQueue.getGroupId(), mdTradeInQueue);
                            tmpQueue.add(mdTradeInQueue);
                            continue;
                        }

                        // 撮合，修改交易信息
                        MdTrade buyerTradeInfo = buyerDTO.createTradeEntity();
                        MdTrade sellerTradeInfo = sellerDTO.createTradeEntity();
                        mdResult = modifyTransInfo(buyerTradeInfo, sellerTradeInfo, currentTransPrice.get(), transNum, mdTrade.getType());
                        transResult.add(mdResult);

                        //将更新后结果重新赋值给新发布交易
                        mdTrade = mdResult.getNewPublishedTrade().copy();
                        mdTradeInQueue = mdResult.getTradeInQueue().copy();

                        // 若队列中的交易还有剩余量，则放回队列继续撮合，否则放入撮合中队列
                        if (mdTradeInQueue.getRemainNum() > 0) {
                            transQueueManager.pushTradeInQueue(mdTradeInQueue.getGroupId(), mdTradeInQueue);
                        }

                        // 只要发生撮合，将交易放入交易中队列
                        if (mdTradeInQueue.getPublishNum() != mdTradeInQueue.getRemainNum()) {
                            transQueueManager.putTradeInOnTradingQueue(mdTradeInQueue);
                        }

                        if (mdTrade.getPublishNum() != mdTrade.getRemainNum()) {
                            transQueueManager.putTradeInOnTradingQueue(mdTrade);
                        }

                        // 是否继续撮合
                        if (mdTrade.getRemainNum() <= 0) {
                            isFinish = true;
                        }
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                        break;
                    }
                }
            }
        } while (!transQueueManager.queueIsEmpty(groupId) && !isFinish);

        // 将为匹配成功的交易放回队列
        if (CollectionUtils.isNotEmpty(tmpQueue)) {
            transQueueManager.pushTradeInQueue(tmpQueue);
        }

        return mdTrade.copy();
    }

    private boolean remainAmountNotEnough(MdTradeDTO buyerDTO, MdTradeDTO sellerDTO, long transNum) {
        if (buyerDTO.getRemainNum() < transNum) {
            StringBuilder errInfo = new StringBuilder();
            errInfo.append("买方剩余量不足，无法完成撮合。【Buy_ID:").append(buyerDTO.getId()).append("】");
            errInfo.append("【Sell_ID:").append(sellerDTO.getId()).append("】");
            errInfo.append("【剩余数量: ").append(buyerDTO.getRemainNum()).append("】");
            errInfo.append("【实际交易数量: ").append(transNum).append("】");
            log.error(errInfo.toString());
            //transQueueManager.pushTradeInQueue(sellerDTO.getGroupId(), sellerDTO);
            return true;
        }

        if (sellerDTO.getRemainNum() < transNum) {
            StringBuilder errInfo = new StringBuilder();
            errInfo.append("卖方剩余量不足，无法完成撮合。【Buy_ID:").append(buyerDTO.getId()).append("】");
            errInfo.append("【Sell_ID:").append(sellerDTO.getId()).append("】");
            errInfo.append("【剩余数量: ").append(sellerDTO.getRemainNum()).append("】");
            errInfo.append("【实际交易数量: ").append(transNum).append("】");
            log.error(errInfo.toString());
            //transQueueManager.pushTradeInQueue(sellerDTO.getGroupId(), sellerDTO);
            return true;
        }

        return false;
    }

    /**
     * 计算剩余量
     *
     * @param buyerDTO
     * @param sellerDTO
     * @param currentTransPrice
     * @param transNum
     * @return
     */
    private boolean remainMoneyNotEnough(MdTradeDTO buyerDTO, MdTradeDTO sellerDTO, Optional<BigDecimal> currentTransPrice, long transNum) {
        if (buyerDTO.getBond().compareTo(currentTransPrice.get().multiply(BigDecimal.valueOf(transNum)).setScale(2, BigDecimal.ROUND_DOWN)) < 0) {
            StringBuilder errInfo = new StringBuilder();
            errInfo.append("买方余额不足，无法完成撮合。【Buy_ID:").append(buyerDTO.getId()).append("】");
            errInfo.append("【Sell_ID:").append(sellerDTO.getId()).append("】");
            errInfo.append("【余额: ").append(buyerDTO.getBond()).append("】");
            errInfo.append("【实际金额: ").append(currentTransPrice.get().multiply(BigDecimal.valueOf(transNum)).setScale(2, BigDecimal.ROUND_DOWN)).append("】");
            log.error(errInfo.toString());
            //transQueueManager.pushTradeInQueue(sellerDTO.getGroupId(), sellerDTO);
            return true;
        }

        return false;
    }

    /**
     * 价格是否不匹配
     *
     * @param mdTrade
     * @param mdTradeInQueue
     * @return
     */
    private boolean priceIsNotMatch(MdTradeDTO mdTrade, MdTradeDTO mdTradeInQueue) {
        if (TradeType.BUY_IN.orignal() == mdTrade.getType()) {
            return mdTrade.getPrice().compareTo(mdTradeInQueue.getPrice()) < 0;
        } else {
            return mdTradeInQueue.getPrice().compareTo(mdTrade.getPrice()) < 0;
        }
    }

    /**
     * 修改买卖双方交易信息
     *
     * @param buyerTradeInfo
     * @param sellerTradeInfo
     * @param currentTransPrice
     * @param transType
     */
    private TransResultDTO modifyTransInfo(MdTrade buyerTradeInfo, MdTrade sellerTradeInfo, BigDecimal currentTransPrice, long transNum, int transType) throws Exception {
        long sellRemainNum = sellerTradeInfo.getRemainNum();
        long buyRemainNum = buyerTradeInfo.getRemainNum();
        if (buyRemainNum <= sellRemainNum) {
            // 剩余数量
            sellerTradeInfo.setRemainNum(sellRemainNum - transNum);
            buyerTradeInfo.setRemainNum(0);

            // 交易状态
            buyerTradeInfo.setState(TradeState.FINISH.orginal());
            sellerTradeInfo.setState(sellerTradeInfo.getRemainNum() <= 0 ? TradeState.FINISH.orginal() : TradeState.ON_TRADING.orginal());

            // 上次交易状态
            buyerTradeInfo.setLastState(LastTransState.NORMAL.orginal());
            sellerTradeInfo.setLastState(sellerTradeInfo.getRemainNum() <= 0 ? LastTransState.NORMAL.orginal() : LastTransState.UNFINISHED.orginal());

            // 余额
            buyerTradeInfo.setBond(BigDecimal.ZERO);
            buyerTradeInfo.setProfit(BigDecimal.ZERO);
        } else {
            // 剩余数量
            buyerTradeInfo.setRemainNum(buyRemainNum - transNum);
            sellerTradeInfo.setRemainNum(0);

            // 交易状态
            sellerTradeInfo.setState(TradeState.FINISH.orginal());
            buyerTradeInfo.setState(TradeState.ON_TRADING.orginal());

            // 上次交易状态
            sellerTradeInfo.setLastState(LastTransState.NORMAL.orginal());
            buyerTradeInfo.setLastState(LastTransState.UNFINISHED.orginal());

            // 扣除手续费
            BigDecimal profit = buyerTradeInfo.getProfit();
            BigDecimal shuoldPay = currentTransPrice.multiply(buyerTradeInfo.getFee()).multiply(BigDecimal.valueOf(transNum)).setScale(2, BigDecimal.ROUND_DOWN);
            if (profit.compareTo(shuoldPay) <= 0) {
                buyerTradeInfo.setProfit(BigDecimal.ZERO);
            } else {
                buyerTradeInfo.setProfit(profit.subtract(shuoldPay));
            }

            // 扣除交易费用
            BigDecimal bond = buyerTradeInfo.getBond();
            BigDecimal shouldPayBond = currentTransPrice.multiply(BigDecimal.valueOf(transNum)).setScale(2, BigDecimal.ROUND_DOWN);
            if (bond.compareTo(shouldPayBond) <= 0) {
                buyerTradeInfo.setBond(BigDecimal.ZERO);
            } else {
                buyerTradeInfo.setBond(bond.subtract(shouldPayBond));
            }

            sellerTradeInfo.setProfit(BigDecimal.ZERO);
        }

        buyerTradeInfo.setUpdateDate(new Date());
        sellerTradeInfo.setUpdateDate(new Date());

        MdTradeDTO publishTrade;
        MdTradeDTO tradeInQueue;
        if (TradeType.BUY_IN.orignal() == transType) {
            publishTrade = new MdTradeDTO(buyerTradeInfo);
            tradeInQueue = new MdTradeDTO(sellerTradeInfo);
        } else {
            publishTrade = new MdTradeDTO(sellerTradeInfo);
            tradeInQueue = new MdTradeDTO(buyerTradeInfo);
        }
        return new TransResultDTO(ActorMsg.TRANS_RESULT_SUCCESS, publishTrade, tradeInQueue, currentTransPrice, transNum, System.nanoTime());
    }
}
