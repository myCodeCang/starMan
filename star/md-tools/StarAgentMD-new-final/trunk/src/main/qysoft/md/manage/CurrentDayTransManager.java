package main.qysoft.md.manage;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import main.qysoft.md.dao.MdTradeMainRepository;
import main.qysoft.md.entity.MdTrade;
import main.qysoft.md.entity.MdTradeMain;
import main.qysoft.md.entity.akkadto.MdTradeDTO;
import main.qysoft.md.entity.akkadto.TransResultDTO;
import main.qysoft.md.service.MdTradeMainServivce;
import main.qysoft.utils.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

/**
 * Created by kevin on 2017/10/24.
 */
@Component("currentDayTransManager")
public class CurrentDayTransManager {
    /**
     * 保存当前交易主表信息
     */
    private Map<String, ConcurrentSkipListSet<MdTradeMain>> currentTransMap = Maps.newConcurrentMap();

    private Map<String, ReentrantLock> locks = Maps.newConcurrentMap();

    private Logger logger = LoggerFactory.getLogger(getClass());

    //private Map<String, Long> groupIdToTradeId = Maps.newHashMap();

    private Map<String, AtomicLong> groupIdToTransNum = Maps.newConcurrentMap();

    @Autowired
    MdTradeMainRepository mdTradeMainRepository;

    @Autowired
    MdTradeMainServivce mdTradeMainServivce;

    private static ReentrantLock rootLock = new ReentrantLock();

    /**
     * 交易列表排序器
     */
    public static Comparator<MdTradeMain> mdTradeMainComparator = Comparator.comparing(MdTradeMain::getUpdateDate);

    /**
     * 更新商品的交易数量
     */
    public synchronized Optional<Long> increamentTransNumByGroupId(String groupId, long delta) {
        if (StringUtils.isEmpty(groupId)) {
            return Optional.empty();
        }

        AtomicLong num = groupIdToTransNum.get(groupId);
        if (num == null) {
            num = new AtomicLong(delta);
            groupIdToTransNum.put(groupId, num);
            return Optional.of(Long.valueOf(num.get()));
        }

        return Optional.of(Long.valueOf(num.addAndGet(delta)));
    }

    /**
     * 将所有商品交易数量清零
     */
    public void emptyAllTransNums() {
        groupIdToTransNum.forEach((groupId, num) -> {
            num.set(0);
        });
    }

    /**
     * 根据撮合的交易更新商品的当天交易信息
     *
     * @param mdTradeMain
     * @param groupId
     * @return
     */
    private void updateCurrentTransInfo(MdTradeMain mdTradeMain, String groupId) {
        if (mdTradeMain == null || StringUtils.isEmpty(groupId)) {
            return;
        }

        ConcurrentSkipListSet<MdTradeMain> transSet = currentTransMap.get(groupId);
        if (null == transSet) {
            transSet = new ConcurrentSkipListSet<>(mdTradeMainComparator);
            currentTransMap.put(groupId, transSet);
        }
        transSet.add(mdTradeMain);
    }

    private ReentrantLock getLock(String groupId) {
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
     * 更新商品交易主信息
     *
     * @param resultDTO
     */
    public void updateMdTradeMain(TransResultDTO resultDTO) {
        BigDecimal currentTransPrice = resultDTO.getCurrentTransPrice();
        MdTradeDTO tradeInQueue = resultDTO.getTradeInQueue();

        String groupId = tradeInQueue.getGroupId();
        ReentrantLock lock = getLock(tradeInQueue.getGroupId());
        lock.lock();
        try {
            Optional<MdTradeMain> newestTradeByGroupId = getNewestTradeByGroupId(groupId);
            MdTradeMain newestTradeInfo;
            if (newestTradeByGroupId.isPresent()) {
                newestTradeInfo = newestTradeByGroupId.get();
            } else {
                Optional<MdTradeMain> currentTransInfoByGroupId = mdTradeMainServivce.findCurrentTransInfoByGroupId(groupId);
                if (currentTransInfoByGroupId.isPresent()) {
                    newestTradeInfo = currentTransInfoByGroupId.get();

                    ConcurrentSkipListSet<MdTradeMain> transSet = currentTransMap.get(groupId);
                    if (null == transSet) {
                        transSet = new ConcurrentSkipListSet<>(mdTradeMainComparator);
                        currentTransMap.put(groupId, transSet);
                    }
                    transSet.add(newestTradeInfo);
                } else {
                    return;
                }
            }

            // 获取缓存中撮合信息和当前撮合结果时间戳，若当前撮合结果早于缓存中撮合结果，直接返回
            long timeStampInResult = resultDTO.getTimeStamp();
            long timeStampInTrade = 0L;
            String remarks = newestTradeInfo.getRemarks();
            if (!StringUtils.isEmpty(remarks)) {
                try {
                    timeStampInTrade = Long.parseLong(remarks);
                } catch (NumberFormatException e) {
                    logger.error(e.getMessage());
                }
            }

            if (timeStampInResult < timeStampInTrade) {
                return;
            }

            BigDecimal highestPrice = newestTradeInfo.getHighestPrice();
            BigDecimal lowestPrice = newestTradeInfo.getLowestPrice();
            if (currentTransPrice.compareTo(highestPrice) > 0) {
                newestTradeInfo.setHighestPrice(currentTransPrice);
            }

            if (currentTransPrice.compareTo(lowestPrice) < 0) {
                newestTradeInfo.setLowestPrice(currentTransPrice);
            }

            newestTradeInfo.setTransactionPrice(currentTransPrice);
            newestTradeInfo.setClosingPrice(currentTransPrice);
            newestTradeInfo.setUpdateDate(new Date());

            Optional<Long> amount = increamentTransNumByGroupId(groupId, resultDTO.getTransNum());
            newestTradeInfo.setAmount(amount.isPresent() ? amount.get() : newestTradeInfo.getAmount());
            newestTradeInfo.setRemarks(String.valueOf(resultDTO.getTimeStamp()));

            //updateCurrentTransInfo(tradeMain, groupId);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 计算成交价
     *
     * @param buyerTradeInfo
     * @param sellerTradeInfo
     * @return
     */
    public Optional<BigDecimal> calculateTransPrice(MdTradeDTO buyerTradeInfo, MdTradeDTO sellerTradeInfo) {
        if (buyerTradeInfo == null || sellerTradeInfo == null) {
            return Optional.empty();
        }

        // 若发布的买和卖相同，则随意返回一个
        if (buyerTradeInfo.getPrice().compareTo(sellerTradeInfo.getPrice()) == 0) {
            Optional.of(buyerTradeInfo.getPrice());
        }

        Date publishBuyDate = buyerTradeInfo.getCreateDate();
        Date publishSellDate = sellerTradeInfo.getCreateDate();
        try {
            int result = DateUtil.compareDate(publishBuyDate, publishSellDate);
            if (result < 0) {
                return Optional.of(buyerTradeInfo.getPrice());
            } else {
                if (0 == result) { // 日期相同，比较ID
                    int compareId = buyerTradeInfo.getId().compareTo(sellerTradeInfo.getId());
                    return Optional.of(compareId < 0 ? buyerTradeInfo.getPrice() : sellerTradeInfo.getPrice());
                } else {
                    return Optional.of(sellerTradeInfo.getPrice());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * 计算成交价
     *
     * @param buyerTradeInfo
     * @param sellerTradeInfo
     * @param groupId
     * @return
     *//*
    public Optional<BigDecimal> calculateTransPrice(MdTrade buyerTradeInfo, MdTrade sellerTradeInfo, String groupId) {
        if (buyerTradeInfo == null || sellerTradeInfo == null || StringUtils.isEmpty(groupId)) {
            return Optional.empty();
        }

        // 若发布的买和卖相同，则随意返回一个
        if (buyerTradeInfo.getPrice().compareTo(sellerTradeInfo.getPrice()) == 0) {
            Optional.of(buyerTradeInfo.getPrice());
        }

        ConcurrentSkipListSet<MdTradeMain> currTransSet = currentTransMap.get(groupId);

        if (CollectionUtils.isEmpty(currTransSet)) {
            MdTradeMain currentTransInfo = mdTradeMainRepository.findCurrentTransInfoByGroupId(groupId);
            if (null == currentTransInfo) {
                // 如果发布的买和卖价格不同，返回买卖平均价
                return Optional.of(buyerTradeInfo.getPrice().add(sellerTradeInfo.getPrice()).divide(BigDecimal.valueOf(2L), 2, BigDecimal.ROUND_HALF_UP));
            }
            synchronized (this) {
                if (currTransSet == null) {
                    currTransSet = new ConcurrentSkipListSet<>(mdTradeMainComparator);
                    currentTransMap.put(groupId, currTransSet);
                    //return Optional.empty();
                }
                currTransSet.add(currentTransInfo);
            }
        }

        MdTradeMain mdTradeMain = currTransSet.last();
        BigDecimal lastTransPrice = mdTradeMain.getTransactionPrice();      // 上一次成交价

        // 如果前一笔成交价低于或等于卖出价，则最新成交价就是卖出价
        if (lastTransPrice.compareTo(sellerTradeInfo.getPrice()) <= 0) {
            return Optional.of(sellerTradeInfo.getPrice());
        }

        // 如果前一笔成交价高于或等于买入价，则最新成交价就是买入价
        if (lastTransPrice.compareTo(buyerTradeInfo.getPrice()) >= 0) {
            return Optional.of(buyerTradeInfo.getPrice());
        }

        // 如果前一笔成交价在卖出价与买入价之间，则最新成交价就是前一笔的成交价
        return Optional.of(lastTransPrice);
    }*/

    /**
     * 清楚所有交易信息，保留最后一条
     */
    private void clearAllTrandInfos(Map<String, MdTradeMain> tradeInfos) {
        boolean tradeInfosIsNotEmpty = MapUtils.isNotEmpty(tradeInfos);
        currentTransMap.forEach((groupId, queue) -> {
            queue.clear();
            if (tradeInfosIsNotEmpty) {
                MdTradeMain tradeMain = tradeInfos.get(groupId);
                if (null != tradeMain) {
                    queue.add(tradeMain);
                }
            }
        });
    }

    public Optional<MdTradeMain> getNewestTradeByGroupId(String groupId) {
        if (StringUtils.isEmpty(groupId)) {
            return Optional.empty();
        }

        ConcurrentSkipListSet<MdTradeMain> mdTradeMains = currentTransMap.get(groupId);
        if (CollectionUtils.isEmpty(mdTradeMains)) {
            return Optional.empty();
        }

        return Optional.of(mdTradeMains.last());
    }

    /**
     * 更新交易信息到数据库
     */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackForClassName = {"RuntimeException", "Exception"})
    public void persistentTrandInfo() {
        List<MdTradeMain> tradeInfos = Lists.newLinkedList();
        currentTransMap.forEach((groupId, queue) -> {
            if (!CollectionUtils.isEmpty(queue)) {
                MdTradeMain last = queue.last();
                if (null != last) {
                    tradeInfos.add(last);
                }
            }
        });

        if (CollectionUtils.isNotEmpty(tradeInfos)) {
            mdTradeMainRepository.save(tradeInfos);
            clearAllTrandInfos(tradeInfos.parallelStream().collect(toMap(MdTradeMain::getGroupId, Function.identity())));
        }
    }


    public synchronized void clearAllTradeMainInfos() {
        currentTransMap.forEach((groupId, queue) -> {
            queue.clear();
        });
    }

    /*public void refreshGroupIdToId(Map<String, Long> newDatas) {
        if (MapUtils.isEmpty(newDatas)) {
            return;
        }

        groupIdToTradeId.clear();;
        groupIdToTradeId.putAll(newDatas);
    }

    public Optional<Long> getTradeIdByGroupId(String groupId) {
        if (StringUtils.isEmpty(groupId)) {
            return Optional.empty();
        }

        return Optional.ofNullable(groupIdToTradeId.get(groupId));
    }*/
}
