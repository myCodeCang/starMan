package com.thinkgem.jeesite.website.task;

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.utils.FileUtils2;
import com.thinkgem.jeesite.modules.md.entity.GoodsStatus;
import com.thinkgem.jeesite.modules.md.entity.MdTrade;
import com.thinkgem.jeesite.modules.md.service.MdTradeService;
import com.thinkgem.jeesite.modules.star.dao.RealTimePriceDao;
import com.thinkgem.jeesite.modules.star.entity.BaseGoodsGroup;
import com.thinkgem.jeesite.modules.star.entity.RealTimePrice;
import com.thinkgem.jeesite.modules.star.service.BaseGoodsGroupService;
import com.thinkgem.jeesite.modules.tasks.entity.UserTaskBusiness;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
@Lazy(false)
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
    public class RefreshOrderListTask implements UserTaskBusiness {

    @Resource
    private BaseGoodsGroupService baseGoodsGroupService ;
    @Resource
    private MdTradeService mdTradeService;
    protected Logger logger = null;
    @Override
    public boolean doBusiness() {
        logger = getLogger();
        logger.error("start..................");
        File path = new File(this.getClass().getResource("/").getPath());
        BaseGoodsGroup goodsGroup = new BaseGoodsGroup();
        goodsGroup.setStatus(GoodsStatus.MD.toString());
        List<BaseGoodsGroup> goodsGroups = baseGoodsGroupService.findList(goodsGroup);
        for (BaseGoodsGroup group : goodsGroups) {
            List<MdTrade> buyGroupListLimit = mdTradeService.getBuyGroupListLimit(group.getId(), 5);
            List<MdTrade> sellGroupListLimit = mdTradeService.getSellGroupListLimit(group.getId(), 5);
            StringBuffer fileName = new StringBuffer();
            Map<String, List<MdTrade>> result = Maps.newHashMap();
            result.put("buyTransList",buyGroupListLimit);
            result.put("sellTransList",sellGroupListLimit);
            fileName.append(path).append(File.separator).append("order").append(File.separator).append(group.getId()).append(".txt");
            String listJson = JsonMapper.getInstance().toJson(result);
            FileUtils2.writeToFile(fileName.toString(),listJson.toString(),false);
        }
        logger.error("end..................");
        return true;
    }
}
