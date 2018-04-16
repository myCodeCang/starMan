package main.qysoft.schedule;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;
import main.qysoft.md.actors.ActorMsg;
import main.qysoft.md.actors.MainActor;
import main.qysoft.md.entity.IsTransDay;
import main.qysoft.md.manage.TransactionQueueManager;
import main.qysoft.utils.SystemOptionsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 定时获取交易，将新的交易发布到相应队列
 * Created by kevin on 2017/10/20.
 */
@Component
public class GetTransactionTask {

    @Autowired
    SystemOptionsUtil systemOptionsUtil;

    @Autowired
    TransactionQueueManager transQueueManager;

    ActorSystem actorSystem = ActorSystem.create("StarAgend", ConfigFactory.load());
    ActorRef mainActor;

    @Scheduled(cron = "0/1 * * * * *")
    public void getTransactionQueue() {
        // 判断是否是交易日
        if (IsTransDay.NO.toString().equals(systemOptionsUtil.isTransDay())) {
            transQueueManager.clearTransQueue();
            return;
        }

        if (mainActor == null) {
            mainActor = actorSystem.actorOf(Props.create(MainActor.class).withDispatcher("my-pinned-dispatcher"), "mainActor-" + UUID.randomUUID());
        }
        mainActor.tell(ActorMsg.TRANS_START, ActorRef.noSender());
        //mainActor.tell(PoisonPill.getInstance(), ActorRef.noSender());
    }

    /**
     * 凌晨1点在系统下架后，清除交易队列
     */
    @Scheduled(cron = "0 32 22 * * *")
    public void clearTransQueue() {
        transQueueManager.clearTransQueue();
    }
}
