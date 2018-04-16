package com.thinkgem.jeesite.website.task;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.modules.star.entity.AuctionGuarantee;
import com.thinkgem.jeesite.modules.star.entity.SaleByAuction;
import com.thinkgem.jeesite.modules.star.service.AuctionGuaranteeService;
import com.thinkgem.jeesite.modules.star.service.SaleByAuctionService;
import com.thinkgem.jeesite.modules.tasks.entity.UserTaskBusiness;
import com.thinkgem.jeesite.modules.user.service.UserUserinfoService;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Service
@Lazy(false)
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class SaleByAuctionTasks implements UserTaskBusiness {

    @Resource
    private SaleByAuctionService saleByAuctionService;
    @Resource
    private AuctionGuaranteeService auctionGuaranteeService;
    @Resource
    private UserUserinfoService userUserinfoService;

    protected Logger logger = null;
    @Override
    public boolean doBusiness() {
        logger = getLogger();
        logger.error("start..................");
        SaleByAuction saleByAuction = new SaleByAuction();
        saleByAuction.setStatus(EnumUtil.YesNO.NO.toString());
        saleByAuction.setEndTimeAfter(new Date());
        AuctionGuarantee auctionGuarantee = new AuctionGuarantee();
        auctionGuarantee.setStatus(EnumUtil.YesNO.NO.toString());
        List<AuctionGuarantee> guaranteeList = auctionGuaranteeService.findList(auctionGuarantee);
        List<SaleByAuction> auctionList = saleByAuctionService.findList(saleByAuction);

        for(SaleByAuction saleAuction : auctionList){
            saleByAuctionService.updateStatus(saleAuction.getId(),EnumUtil.YesNO.YES.toString());
            List<AuctionGuarantee> auctionGuarantees = guaranteeList.stream().filter(p -> p.getAuctionId().equals(saleAuction.getId())).collect(Collectors.toList());
            for(AuctionGuarantee guarantee:auctionGuarantees){
                //标记退还
                auctionGuaranteeService.updateStatus(guarantee.getId(),EnumUtil.YesNO.YES.toString());
                //保证金退换
                userUserinfoService.updateUserMoney(guarantee.getUserName(),guarantee.getMoney(),"保证金退还,拍卖编号:"+guarantee.getAuctionId(), EnumUtil.MoneyChangeType.AUCTION_GUARANTEE_BACK);
            }
        }
        logger.error("end..................");

        return true;
    }




}
