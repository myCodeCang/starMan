package com.thinkgem.jeesite.website.task;

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.utils.FileUtils2;
import com.thinkgem.jeesite.modules.md.entity.GoodsStatus;
import com.thinkgem.jeesite.modules.md.entity.MdTrade;
import com.thinkgem.jeesite.modules.md.entity.MdTradeLog;
import com.thinkgem.jeesite.modules.md.service.MdTradeLogService;
import com.thinkgem.jeesite.modules.md.service.MdTradeService;
import com.thinkgem.jeesite.modules.star.entity.BaseGoodsGroup;
import com.thinkgem.jeesite.modules.star.service.BaseGoodsGroupService;
import com.thinkgem.jeesite.modules.tasks.entity.UserTaskBusiness;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
@Lazy(false)
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class RefreshdealLogTask implements UserTaskBusiness {

    @Resource
    private BaseGoodsGroupService baseGoodsGroupService ;
    @Resource
    private MdTradeLogService mdTradeLogService;
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
            List<MdTradeLog> logList = mdTradeLogService.realTimeList(group.getId(),new Date(),10);
            StringBuffer fileName = new StringBuffer();
            Map<String, List<MdTradeLog>> result = Maps.newHashMap();
            result.put("logList",logList);
            fileName.append(path).append(File.separator).append("dealLog").append(File.separator).append(group.getId()).append(".txt");
            String listJson = JsonMapper.getInstance().toJson(result);
            FileUtils2.writeToFile(fileName.toString(),listJson.toString(),false);
        }
        logger.error("end..................");
        return true;
    }
}
