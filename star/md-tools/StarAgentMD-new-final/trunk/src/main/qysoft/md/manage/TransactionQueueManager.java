package main.qysoft.md.manage;

import com.google.common.collect.Maps;
import main.qysoft.md.entity.TradeType;
import main.qysoft.md.entity.akkadto.MdTradeDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.stream.Collectors.toList;

/**
 * Created by kevin on 2017/10/21.
 */
@Component("transQueueManager")
public class TransactionQueueManager {
    //上次获取的交易最大ID，作为下次获取交易信息的查询条件
    private Map<String, AtomicLong> lastTransIds = Maps.newConcurrentMap();

    // 当前撮合所操作队列类型
    private Map<String, TradeType> tradeTypes = Maps.newConcurrentMap();

    /**
     * 正在撮合中的交易队列
      */
    private Map<String, ConcurrentLinkedQueue<MdTradeDTO>> onTradingMap = Maps.newConcurrentMap();

    /**
     * 所有商品的买队列，以商品ID为Key，交易信息为value
     */
    private Map<String, ConcurrentSkipListSet<MdTradeDTO>> buyQueueMap = Maps.newConcurrentMap();

    /**
     * 所有商品的卖队列，以商品ID为Key，交易信息为value
     */
    private Map<String, ConcurrentSkipListSet<MdTradeDTO>> sellQueueMap = Maps.newConcurrentMap();

    /**
     * 撮合锁
     */
    private Map<String, ReentrantLock> locks = Maps.newConcurrentMap();
    private static ReentrantLock rootLock = new ReentrantLock();

    private Logger logger = LoggerFactory.getLogger(getClass());

    public ReentrantLock getLock(String groupId) {
        rootLock.lock();
        try {
            ReentrantLock newLock = locks.get(groupId);
            if (null == newLock) {
                newLock = new ReentrantLock();
                locks.put(groupId, newLock);
            }

            return newLock;
        } finally {
            rootLock.unlock();
        }
    }

    /**
     * 将交易放入撮合中队列
     * @param trade
     */
    public synchronized void putTradeInOnTradingQueue(MdTradeDTO trade) {
        if (null == trade) {
            return;
        }

        String groupId = trade.getGroupId();
        try {
            ConcurrentLinkedQueue<MdTradeDTO> onTradingQuene = onTradingMap.get(groupId);
            if (onTradingQuene == null) {
                onTradingQuene = new ConcurrentLinkedQueue<>();
                onTradingMap.put(groupId, onTradingQuene);
            }

            Optional<MdTradeDTO> onTradingTrade = onTradingQuene.parallelStream().filter(tradeInQueue -> tradeInQueue.getId().equals(trade.getId())).findAny();
            if (onTradingTrade.isPresent()) {
                return;
            }

            onTradingQuene.add(trade);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 交易是否正在撮合中
     * @param groupId
     * @param tradeId
     * @return
     */
    public boolean tradeIsOnTrading(String groupId, Long tradeId) {
        if (StringUtils.isEmpty(groupId) || null == tradeId) {
            return false;
        }

        ConcurrentLinkedQueue<MdTradeDTO> mdTradeDTOS = onTradingMap.get(groupId);
        if (CollectionUtils.isEmpty(mdTradeDTOS)) {
            return false;
        }

        Optional<MdTradeDTO> onTradingTrade = mdTradeDTOS.parallelStream().filter(tradeInQueue -> tradeInQueue.getId().equals(tradeId)).findAny();
        if (onTradingTrade.isPresent()) {
            /*MdTradeDTO tradeOnTrading = onTradingTrade.get();
            if (tradeOnTrading.getRemainNum() <= 0) {
                removeOnTradingTrade(tradeOnTrading);
                return false;
            }*/

            /*if (!tradeIsInQueue(groupId, tradeId)) {
                removeOnTradingTrade(tradeOnTrading);
                return false;
            }*/
            return true;
        }

        return false;
    }

    /**
     * 交易是否在待撮合队列
     * @param groupId
     * @param tradeId
     * @return
     */
    public boolean tradeIsInQueue(String groupId, Long tradeId) {
        if (StringUtils.isEmpty(groupId) || null == tradeId) {
            return false;
        }

        ConcurrentSkipListSet<MdTradeDTO> buyQueue = buyQueueMap.get(groupId);
        ConcurrentSkipListSet<MdTradeDTO> sellQueue = sellQueueMap.get(groupId);
        if (CollectionUtils.isEmpty(buyQueue) && CollectionUtils.isEmpty(sellQueue)) {
            return false;
        }

        Optional<MdTradeDTO> tradeDTO = buyQueue.parallelStream().filter(tradeInQueue -> tradeInQueue.getId().equals(tradeId)).findAny();
        if (tradeDTO.isPresent()) {
            return true;
        }

        tradeDTO = sellQueue.parallelStream().filter(tradeInQueue -> tradeInQueue.getId().equals(tradeId)).findAny();
        if (tradeDTO.isPresent()) {
            return true;
        }

        return false;
    }

    /**
     * 将交易从撮合中队列移除
     * @param trade
     */
    public void removeOnTradingTrade(MdTradeDTO trade) {
        if (null == trade) {
            return;
        }

        String groupId = trade.getGroupId();
        ConcurrentLinkedQueue<MdTradeDTO> mdTradeDTOS = onTradingMap.get(groupId);
        if (CollectionUtils.isEmpty(mdTradeDTOS)) {
            return;
        }

        Optional<MdTradeDTO> onTradingTrade = mdTradeDTOS.parallelStream().filter(tradeInQueue -> tradeInQueue.getId().equals(trade.getId())).findAny();
        if (onTradingTrade.isPresent()) {
            mdTradeDTOS.remove(onTradingTrade.get());
        }
    }

    /**
     * 获取当前应该撮合的队列类型
     * @param groupId
     * @return
     */
    public synchronized Optional<TradeType> getTradeType(String groupId) {
        if (StringUtils.isEmpty(groupId)) {
            return Optional.empty();
        }

        return Optional.ofNullable(tradeTypes.get(groupId));
    }

    /**
     * 设置下次撮合的队列类型
     * @param groupId
     * @param tradeType
     */
    public synchronized void setTradeType(String groupId, TradeType tradeType) {
        if (StringUtils.isEmpty(groupId) || null == tradeType) {
            return;
        }

        tradeTypes.put(groupId,tradeType);
    }

    /**
     * 交易列表排序器
     */
    private Comparator<MdTradeDTO> sellOutComparator = (m1, m2) -> {
        int priceCompare = m1.getPrice().compareTo(m2.getPrice());
        if (priceCompare != 0) {
            return priceCompare;
        }
        int compareDate = m1.getCreateDate().compareTo(m2.getCreateDate());
        return compareDate == 0 ? m1.getId().compareTo(m2.getId()) : compareDate;
    };

    private Comparator<MdTradeDTO> buyInComparator = (m1, m2) -> {
        int priceCompare = m1.getPrice().compareTo(m2.getPrice());
        if (priceCompare != 0) {
            return priceCompare;
        }
        int compareDate = m1.getCreateDate().compareTo(m2.getCreateDate());
        return compareDate == 0 ? (m1.getId().compareTo(m2.getId())) : compareDate;
    };

    public long getLastQueryTransId(String groupId) {
        if (StringUtils.isEmpty(groupId)) {
            return 0L;
        }

        AtomicLong atomicLong = lastTransIds.get(groupId);
        if (null == atomicLong) {
            atomicLong = new AtomicLong(0L);
            lastTransIds.put(groupId, atomicLong);
        }
        return atomicLong.get();
    }

    public void setLastTransId(String groupId, long newId) {
        if (StringUtils.isEmpty(groupId)) {
            return;
        }

        AtomicLong atomicLong = lastTransIds.get(groupId);
        if (null == atomicLong) {
            atomicLong = new AtomicLong(0L);
            lastTransIds.put(groupId, atomicLong);
        }
        atomicLong.set(newId);
    }

    /**
     * 获取买队列价格最高的交易
     * @param groupId
     * @return
     */
    public Optional<MdTradeDTO> popBuyIn(String groupId) {
        if (StringUtils.isEmpty(groupId)) {
            return Optional.empty();
        }

        ConcurrentSkipListSet<MdTradeDTO> buyQueue = buyQueueMap.get(groupId);
        if (CollectionUtils.isEmpty(buyQueue)) {
            return Optional.empty();
        }

        return Optional.ofNullable(buyQueue.pollLast());
    }

    /**
     * 将发布的交易放入买入队列
     * @param groupId
     * @param mdTradeDTO
     * @return
     */
    public synchronized boolean pushBuyIn(String groupId, MdTradeDTO mdTradeDTO) {
        if (StringUtils.isEmpty(groupId) || mdTradeDTO == null) {
            return false;
        }

        try {
            ConcurrentSkipListSet<MdTradeDTO> buyQueue = buyQueueMap.get(groupId);
            if (buyQueue == null) {
                buyQueue = new ConcurrentSkipListSet<>(buyInComparator);
                buyQueueMap.put(groupId, buyQueue);
            }

            Optional<MdTradeDTO> matchedTrade = buyQueue.parallelStream().filter(trade -> mdTradeDTO.getId().equals(trade.getId())).findAny();
            if (!matchedTrade.isPresent()) {
                buyQueue.add(mdTradeDTO);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * 获取卖队列价格最低的交易`
     * @param groupId
     * @return
     */
    public Optional<MdTradeDTO> popSellOut(String groupId) {
        if (StringUtils.isEmpty(groupId)) {
            return Optional.empty();
        }

        ConcurrentSkipListSet<MdTradeDTO> sellQueue = sellQueueMap.get(groupId);
        if (CollectionUtils.isEmpty(sellQueue)) {
            return Optional.empty();
        }

        return Optional.ofNullable(sellQueue.pollFirst());
    }

    /**
     * 将发布的交易放入卖出队列
     * @param groupId
     * @param mdTradeDTO
     * @return
     */
    public synchronized boolean pushSellOut(String groupId, MdTradeDTO mdTradeDTO) {
        if (StringUtils.isEmpty(groupId) || mdTradeDTO == null) {
            return false;
        }

        try {
            ConcurrentSkipListSet<MdTradeDTO> sellQueue = sellQueueMap.get(groupId);
            if (sellQueue == null) {
                sellQueue = new ConcurrentSkipListSet<>(sellOutComparator);
                sellQueueMap.put(groupId, sellQueue);
            }

            Optional<MdTradeDTO> matchedTrade = sellQueue.parallelStream().filter(trade -> mdTradeDTO.getId().equals(trade.getId())).findAny();
            if (!matchedTrade.isPresent()) {
                sellQueue.add(mdTradeDTO);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }

        return true;
    }

    public boolean pushTradeInQueue(String groupId, MdTradeDTO trade) {
        if (StringUtils.isEmpty(groupId) || trade == null) {
            return false;
        }

        if (TradeType.SELL_OUT.orignal() == trade.getType()) {
            return pushSellOut(groupId, trade);
        } else {
            return pushBuyIn(groupId, trade);
        }
    }

    public boolean pushTradeInQueue(Collection<MdTradeDTO> trades) {
        if (CollectionUtils.isEmpty(trades)) {
            return false;
        }

        trades.forEach(trade -> {
            String groupId = trade.getGroupId();
            if (TradeType.SELL_OUT.orignal() == trade.getType()) {
                pushSellOut(groupId, trade);
            } else {
                pushBuyIn(groupId, trade);
            }
        });

        return true;
    }

    /**
     * 从队列中删除指定交易
     * @param trades
     */
    public void removeTrades(Collection<MdTradeDTO> trades) {
        sellQueueMap.forEach((groupId, queue) -> {
            queue.removeAll(trades);
        });

        buyQueueMap.forEach((group, queue) -> {
            queue.removeAll(trades);
        });

        onTradingMap.forEach((groupId, queue) -> {
            queue.removeAll(trades);
        });
    }

    /**
     * 买或卖撮合队列是否为空
     * @return
     */
    public boolean queueIsEmpty(String groupId) {
        if (StringUtils.isEmpty(groupId)) {
            return false;
        }

        return CollectionUtils.isEmpty(buyQueueMap.get(groupId)) || CollectionUtils.isEmpty(sellQueueMap.get(groupId));
    }

    public void resetAllGroupGoodsLastTransId() {
        lastTransIds.values().forEach(id -> id.set(0L));
    }

    /**
     * 清除所有交易队列
     */
    public synchronized void clearTransQueue() {
        buyQueueMap.forEach((groupId, queue) -> {
            queue.clear();
        });

        sellQueueMap.forEach((groupId, queue) -> {
            queue.clear();
        });


        onTradingMap.forEach((groupId, queue) -> {
            queue.clear();
        });
    }
}
