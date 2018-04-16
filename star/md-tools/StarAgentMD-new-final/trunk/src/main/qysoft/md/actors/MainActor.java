package main.qysoft.md.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import main.Main;
import main.qysoft.md.entity.TradeIsOverTop;
import main.qysoft.md.manage.TransactionQueueManager;
import main.qysoft.md.service.MdTradeMainServivce;
import main.qysoft.md.service.MdTradeService;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MainActor extends UntypedAbstractActor {
    protected final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    MdTradeService mdTradeService = Main.applicationContext.getBean(MdTradeService.class);

    MdTradeMainServivce mdTradeMainServivce = Main.applicationContext.getBean(MdTradeMainServivce.class);

    @Override
    public void onReceive(Object msg) throws Throwable {
        if (ActorMsg.TRANS_START == msg) {
            try {
                //获取所有已发布的商品ID
                List<Integer> publishedGroupIds = mdTradeService.findPublishedGroupIds();
                if (CollectionUtils.isEmpty(publishedGroupIds)) {
                    return;
                }

                publishedGroupIds.forEach((groupId) -> {
                    /*Optional<String> transInfo = mdTradeMainServivce.findIsOverTop(groupId.toString());
                    if (transInfo.isPresent() && TradeIsOverTop.YES.toString().equals(transInfo.get())) {
                        return;
                    }*/

                    ActorRef marriedActor = getContext().actorOf(Props.create(MarriedDealActor.class).withDispatcher("my-pinned-dispatcher"), "marriedActor-" + UUID.randomUUID());
                    marriedActor.tell(groupId.toString(), getSelf());
                });
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } else if (msg instanceof List) {
            ActorRef handleMdResultActor = getContext().actorOf(Props.create(HandleMdResultActor.class).withDispatcher("my-pinned-dispatcher"), "handleMdResultActor-" + UUID.randomUUID());
            handleMdResultActor.tell(msg, getSelf());
        } else if (msg == ActorMsg.TRANS_RESULT_SUCCESS) {
            getContext().stop(getSender());
        } else {
            unhandled(msg);
        }
    }
}
