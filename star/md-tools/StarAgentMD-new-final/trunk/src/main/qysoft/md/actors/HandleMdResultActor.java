package main.qysoft.md.actors;

import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import main.Main;
import main.qysoft.md.entity.RealTimePrice;
import main.qysoft.md.entity.akkadto.MdTradeDTO;
import main.qysoft.md.entity.akkadto.TransResultDTO;
import main.qysoft.md.manage.CurrentDayTransManager;
import main.qysoft.md.manage.TransactionQueueManager;
import main.qysoft.md.service.MdTradeMainServivce;
import main.qysoft.md.service.MdTradeService;
import main.qysoft.md.service.RealTimePriceService;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.List;

/**
 * 完成发布交易的Actor
 * Created by kevin on 2017/10/21.
 */
public class HandleMdResultActor extends UntypedAbstractActor {
    protected final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    CurrentDayTransManager currentDayTransManager = Main.applicationContext.getBean(CurrentDayTransManager.class);

    TransactionQueueManager transQueueManager = Main.applicationContext.getBean(TransactionQueueManager.class);

    MdTradeService mdTradeService = Main.applicationContext.getBean(MdTradeService.class);


    @Override
    public void onReceive(Object msg) throws Throwable {
        if (msg instanceof List) {
            if (CollectionUtils.isEmpty((Collection<?>) msg)) {
                getContext().stop(getSelf());
            }

            Object transResult = ((List) msg).get(0);
            if (!(transResult instanceof TransResultDTO)) {
                getContext().stop(getSelf());
            }

            ((List) msg).forEach(resultDTO -> {
                TransResultDTO result = (TransResultDTO) resultDTO;
                try {
                    // 更新交易主表
                    currentDayTransManager.updateMdTradeMain(result);

                    //处理返回的队列中交易
                    mdTradeService.handleMdTradeResult(result);

                    // 若交易剩余量为零，从交易中队列中清除
                    MdTradeDTO newPublishedTrade = result.getNewPublishedTrade();
                    MdTradeDTO tradeInQueue = result.getTradeInQueue();
                    if (newPublishedTrade.getRemainNum() <= 0) {
                        transQueueManager.removeOnTradingTrade(newPublishedTrade);
                    }

                    if (tradeInQueue.getRemainNum() <= 0) {
                        transQueueManager.removeOnTradingTrade(tradeInQueue);
                    }
                } catch (Exception ex) {
                    currentDayTransManager.increamentTransNumByGroupId(result.getTradeInQueue().getGroupId(), result.getTransNum() * -1);
                    log.error(ex.getMessage());
                }
            });

            /*getContext().stop(getSelf());*/
            getSender().tell(ActorMsg.TRANS_RESULT_SUCCESS, getSelf());
        } else {
            unhandled(msg);
        }
    }
}
