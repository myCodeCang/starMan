package main.qysoft.schedule;

import main.qysoft.md.dao.MdTradeMainRepository;
import main.qysoft.md.entity.IsTransDay;
import main.qysoft.md.manage.CurrentDayTransManager;
import main.qysoft.md.manage.TransactionQueueManager;
import main.qysoft.utils.SystemOptionsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 交易主表管理任务
 * Created by kevin on 2017/10/26.
 */
@Component
public class TradeMainMgrTask {
    @Autowired
    TransactionQueueManager transQueueManager;

    @Autowired
    CurrentDayTransManager currentDayTransManager;

    @Autowired
    MdTradeMainRepository mdTradeMainRepository;

    @Autowired
    SystemOptionsUtil systemOptions;

    /*@Scheduled(cron = "0 0 0/1 * * *")
    public void refreshMdTradeMain() {
        List<MdTradeMain> currentDayTrans = mdTradeMainRepository.findCurrentDayTrans();
        if (CollectionUtils.isEmpty(currentDayTrans)) {
            return;
        }

        Map<String, Long> groupIdToId = currentDayTrans.parallelStream().collect(Collectors.toMap(MdTradeMain::getGroupId, MdTradeMain::getId));
        if (MapUtils.isEmpty(groupIdToId)) {
            return;
        }

        currentDayTransManager.refreshGroupIdToId(groupIdToId);
    }*/

    @Scheduled(cron = "0/2 * * * * *")
    public void updateTradeMain() {
        // 根据缓存中内容更新交易主表
        if (IsTransDay.NO.toString().equals(systemOptions.isTransDay())) {
            return;
        }

        currentDayTransManager.persistentTrandInfo();
    }

    /**
     * 在每天开盘前（凌晨4点），初始化交易主表
     */
    @Scheduled(cron = "0 0 4 * * *")
    public void initMdTradeMain() {
        // 重置最后交易ID
        transQueueManager.resetAllGroupGoodsLastTransId();

        /*List<MdTradeMain> lastDayTrans = mdTradeMainRepository.findLastDayTrans();
        if (CollectionUtils.isEmpty(lastDayTrans)) {
            return;
        }

        List<MdTradeMain> currentDayTrans = Lists.newLinkedList();
        lastDayTrans.forEach(md -> {
            MdTradeMain newTrade = new MdTradeMain();
            newTrade.setAmount(0);
            newTrade.setClosingPrice(BigDecimal.ZERO);
            newTrade.setCreateDate(new Date());
            newTrade.setGroupId(md.getGroupId());
            newTrade.setHighestPrice(BigDecimal.ZERO);
            newTrade.setLowestPrice(BigDecimal.ZERO);
            newTrade.setOpeningPrice(md.getClosingPrice());
            newTrade.setIsOverTop(TradeIsOverTop.NO.toString());
            newTrade.setTransactionPrice(md.getClosingPrice());
            newTrade.setIsTransDay(systemOptions.isTransDay());

            // 初始化主表id Map

            currentDayTrans.add(newTrade);
        });
        mdTradeMainRepository.save(currentDayTrans);*/

        currentDayTransManager.clearAllTradeMainInfos();

        // 商品交易量清空
        currentDayTransManager.emptyAllTransNums();
    }
}
