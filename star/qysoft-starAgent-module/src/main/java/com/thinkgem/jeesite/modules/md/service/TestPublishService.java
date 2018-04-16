package com.thinkgem.jeesite.modules.md.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.md.dao.MdTradeLogDao;
import com.thinkgem.jeesite.modules.md.entity.MdTradeLog;
import com.thinkgem.jeesite.modules.md.entity.TradeType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by kevin on 2017/10/2.
 */
@Component
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackForClassName = {"RuntimeException", "Exception", "ValidationException"})
public class TestPublishService extends CrudService<MdTradeLogDao, MdTradeLog> {
    public MdTradeLog get(String id) {
        return super.get(id);
    }

    @Resource
    private MdTradeService mdTradeService;

    private BigDecimal[] priceBuy = new BigDecimal[]{
            BigDecimal.valueOf(9.5),
            BigDecimal.valueOf(9.88),
            BigDecimal.valueOf(9.7),
            BigDecimal.valueOf(10.47),
            BigDecimal.valueOf(10.54),
            BigDecimal.valueOf(10.36),
            BigDecimal.valueOf(10.1),
            BigDecimal.valueOf(10.2),
            BigDecimal.valueOf(10.3),
            BigDecimal.valueOf(10.4),
            BigDecimal.valueOf(10.52),
            BigDecimal.valueOf(10.63),
            BigDecimal.valueOf(10.74),
            BigDecimal.valueOf(10.85),
            BigDecimal.valueOf(10.96),
            BigDecimal.valueOf(11.03),
            BigDecimal.valueOf(11.21),
            BigDecimal.valueOf(11.60),
    };

    private BigDecimal[] priceSell = new BigDecimal[]{
            BigDecimal.valueOf(9.5),
            BigDecimal.valueOf(9.88),
            BigDecimal.valueOf(9.7),
            BigDecimal.valueOf(10.47),
            BigDecimal.valueOf(10.54),
            BigDecimal.valueOf(10.36),
            BigDecimal.valueOf(10.1),
            BigDecimal.valueOf(10.2),
            BigDecimal.valueOf(10.3),
            BigDecimal.valueOf(10.4),
            BigDecimal.valueOf(10.52),
            BigDecimal.valueOf(10.63),
            BigDecimal.valueOf(10.74),
            BigDecimal.valueOf(10.85),
            BigDecimal.valueOf(10.96),
            BigDecimal.valueOf(11.03),
            BigDecimal.valueOf(11.21),
            BigDecimal.valueOf(11.60),
    };

    private String[] transType = new String[] {
            TradeType.BUY_IN.toString(),
            TradeType.SELL_OUT.toString()
    };

    private int[] number = new int[] {100, 204, 512, 615, 747, 204, 136, 204, 289, 369,458};

    public void doSomeThing() {
        test1();

    }
    public void test1(){
        Random random = new Random(System.currentTimeMillis());
        int publishNum = number[random.nextInt(number.length)];
        String publishType = transType[random.nextInt(transType.length)];
        String[] tickets = new String[]{"68096","68277","68391","68392"};
        String[] users = new String[]{"hy351424","hy574731","hy139275","hy738839","hy986589","hy930704","hy173423","hy206656","hy525267","hy256848"};
        String userName = users[random.nextInt(9)];
        String groupId = tickets[random.nextInt(3)];
        try {
            BigDecimal publishPrice;
            if (publishType.equals(TradeType.BUY_IN.toString())) {
                publishPrice = priceBuy[random.nextInt(priceBuy.length)];
                mdTradeService.publishBuy(userName, groupId, publishPrice, publishNum, "balance");
            } else {
                publishPrice = priceSell[random.nextInt(priceSell.length)];
                mdTradeService.publishSell(userName, groupId, publishPrice, publishNum, "balance");
            }
            //logger.error(userName + "发布" + buyOrSell + ", 数量:" + publishNum + " 价格:" + publishPrice);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}

