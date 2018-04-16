package com.thinkgem.jeesite.website.task;

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.utils.FileUtils2;
import com.thinkgem.jeesite.modules.md.entity.GoodsStatus;
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
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackForClassName = {"RuntimeException", "Exception", "ValidationException"})
public class GetNowPriceTask implements UserTaskBusiness {

    @Resource
    private BaseGoodsGroupService baseGoodsGroupService;
    @Resource
    private RealTimePriceDao realTimePriceDao;
    protected Logger logger = null;

    @Override
    public boolean doBusiness() {
        logger = getLogger();
        logger.error("start..................");
        File path = new File(this.getClass().getResource("/").getPath());
        List<RealTimePrice> realTimePrice = realTimePriceDao.getRealTimePrice();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        //插入最新价
        for (RealTimePrice group : realTimePrice) {
            StringBuffer fileName = new StringBuffer();
            fileName.append(path).append(File.separator).append("nowprice").append(File.separator).append(group.getGroupId()).append(".txt");
            Map<String, String> result = Maps.newHashMap();
            result.put("price", group.getPrice().toString());
            result.put("nowTime", dateFormat.format(new Date()));
            String jsonStr = JsonMapper.getInstance().toJson(result);
            FileUtils2.writeToFile(fileName.toString(), jsonStr, false);
        }
        logger.error("end..................");
        return true;
    }


}
