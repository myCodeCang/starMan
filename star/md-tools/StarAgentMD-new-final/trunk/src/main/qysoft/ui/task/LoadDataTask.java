package main.qysoft.ui.task;

import javafx.concurrent.Task;
import main.Main;
import main.qysoft.md.entity.BaseGoodsGroup;
import main.qysoft.md.entity.MdTrade;
import main.qysoft.md.entity.MdTradeLog;
import main.qysoft.md.entity.MdTradeMain;
import main.qysoft.md.service.BaseGoodsGroupService;
import main.qysoft.md.service.MdTradeLogService;
import main.qysoft.md.service.MdTradeMainServivce;
import main.qysoft.md.service.MdTradeService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 加载数据Task
 */
public class LoadDataTask extends Task<Integer> {

    public static final Integer LOAD_STATUS_SUCCESS = Integer.valueOf(1);
    public static final Integer LOAD_STATUS_FAILD = Integer.valueOf(0);

    private List<MdTrade> tradeList;
    private List<MdTradeLog> tradeLogList;
    private Page<MdTradeLog> tradeLogFilterList;
    private MdTradeMain tradeMain;
    private List<BaseGoodsGroup> baseGoodsGroupList;
    private List<MdTradeMain> mdTradeMainList;

    private Long goodGroupId;

    public LoadDataTask(Long goodGroupId) {
        this.goodGroupId = goodGroupId;
    }

    @Override
    protected Integer call() throws Exception {
        return loadData("", null, null, 1);
    }

    public Integer loadData(String userName, LocalDate beginDate, LocalDate endDate, int pageNum) {
        MdTradeMainServivce mdTradeMainServivce = Main.applicationContext.getBean(MdTradeMainServivce.class);
        MdTradeService mdTradeService = Main.applicationContext.getBean(MdTradeService.class);
        MdTradeLogService tradeLogService = Main.applicationContext.getBean(MdTradeLogService.class);
        BaseGoodsGroupService baseGoodsGroupService = Main.applicationContext.getBean(BaseGoodsGroupService.class);

        // 获取下拉列表商品信息
        baseGoodsGroupList = baseGoodsGroupService.findAllGoodsOnTrading();
        if (CollectionUtils.isEmpty(baseGoodsGroupList)) {
            return LoadDataTask.LOAD_STATUS_FAILD;
        }

        // 获取商品ID
        if (null == goodGroupId) {
            if (CollectionUtils.isEmpty(baseGoodsGroupList)) {
                return LoadDataTask.LOAD_STATUS_FAILD;
            }
            goodGroupId = baseGoodsGroupList.get(0).getId();
        }
        // 获取主交易信息
        Optional<MdTradeMain> currentTransInfoByGroupId = mdTradeMainServivce.findCurrentTransInfoByGroupId(goodGroupId.toString());
        if (currentTransInfoByGroupId.isPresent()) {
            tradeMain = currentTransInfoByGroupId.get();
        }
        //获取主表list
        List<String> groupIds = baseGoodsGroupList.stream().map(p -> String.valueOf(p.getId())).collect(Collectors.toList());
        mdTradeMainList = mdTradeMainServivce.findByGroupIdInAndCreateDate_Day(groupIds);
        // 获取发布的交易信息
        tradeList = mdTradeService.getNewPublishedTrans(goodGroupId.toString(), 0L);
        // 获取已完成的交易
        tradeLogList = tradeLogService.findByGroupId(goodGroupId.intValue());
        //按条件筛选交易记录
        tradeLogFilterList = tradeLogService.findByFilterCondition(goodGroupId.toString(), userName, beginDate, endDate, pageNum);

        if (CollectionUtils.isEmpty(tradeLogList)) {
            return LoadDataTask.LOAD_STATUS_FAILD;
        }
        if (CollectionUtils.isEmpty(tradeList)) {
            return LoadDataTask.LOAD_STATUS_FAILD;
        }
        if (!currentTransInfoByGroupId.isPresent()) {
            return LoadDataTask.LOAD_STATUS_FAILD;
        }

        return LOAD_STATUS_SUCCESS;

    }

    public List<MdTrade> getTradeList() {
        return tradeList;
    }

    public List<MdTradeLog> getTradeLogList() {
        return tradeLogList;
    }

    public MdTradeMain getTradeMain() {
        return tradeMain;
    }

    public List<BaseGoodsGroup> getBaseGoodsGroupList() {
        return baseGoodsGroupList;
    }

    public Long getGoodGroupId() {
        return goodGroupId;
    }

    public List<MdTradeMain> getMdTradeMainList() {
        return mdTradeMainList;
    }

    public Page<MdTradeLog> getTradeLogFilterList() {
        return tradeLogFilterList;
    }
}
