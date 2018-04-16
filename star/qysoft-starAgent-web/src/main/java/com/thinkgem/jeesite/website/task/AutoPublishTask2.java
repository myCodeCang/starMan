package com.thinkgem.jeesite.website.task;

import com.thinkgem.jeesite.modules.md.entity.TradeType;
import com.thinkgem.jeesite.modules.md.service.MdTradeService;
import com.thinkgem.jeesite.modules.tasks.entity.UserTaskBusiness;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Random;

@Service
@Lazy(false)
@Transactional(readOnly = false, propagation= Propagation.REQUIRED, rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class AutoPublishTask2 implements UserTaskBusiness {

    @Resource
    private MdTradeService mdTradeService;

    protected Logger logger = null;

    private BigDecimal[] priceBuy = new BigDecimal[]{
            BigDecimal.valueOf(0.6),
            BigDecimal.valueOf(0.7),
            BigDecimal.valueOf(0.8),
            BigDecimal.valueOf(0.9),
            BigDecimal.valueOf(1.0),
            BigDecimal.valueOf(1.3),
            BigDecimal.valueOf(1.6),
            BigDecimal.valueOf(1.8),
            BigDecimal.valueOf(2.0)
    };

    private BigDecimal[] priceSell = new BigDecimal[]{
            BigDecimal.valueOf(0.5),
            BigDecimal.valueOf(0.6),
            BigDecimal.valueOf(0.7),
            BigDecimal.valueOf(0.8),
            BigDecimal.valueOf(1.0),
            BigDecimal.valueOf(1.2),
            BigDecimal.valueOf(1.5),
            BigDecimal.valueOf(1.8),
            BigDecimal.valueOf(1.9)
    };

    private String[] transType = new String[] {
            TradeType.BUY_IN.toString(),
            TradeType.SELL_OUT.toString()
    };

    private int[] number = new int[] {5, 15, 20, 30, 50, 60, 80, 100, 120, 150};

    private String userName = "hy206656";

    private String groupId = "68392";

    @Override
    public boolean doBusiness() {
        logger = getLogger();

        Random random = new Random(System.currentTimeMillis());

        int publishNum = number[random.nextInt(number.length)];
        String publishType = transType[random.nextInt(transType.length)];

        try {
            BigDecimal publishPrice;
            if (publishType.equals(TradeType.BUY_IN.toString())) {
                publishPrice = priceBuy[random.nextInt(priceBuy.length)];
                mdTradeService.publishBuy(userName, groupId, publishPrice, publishNum, "1");
            } else {
                publishPrice = priceSell[random.nextInt(priceSell.length)];
                mdTradeService.publishSell(userName, groupId, publishPrice, publishNum, "1");
            }

            //logger.error(userName + "发布" + buyOrSell + ", 数量:" + publishNum + " 价格:" + publishPrice);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return true;
    }
}
