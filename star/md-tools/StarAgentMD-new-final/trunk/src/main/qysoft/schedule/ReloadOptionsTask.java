package main.qysoft.schedule;

import main.qysoft.utils.SystemOptionsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 每隔9秒钟更新一次撮合参数，避免系统后台修改参数，与撮合模块不同步
 * Created by kevin on 2017/10/25.
 */
@Component
public class ReloadOptionsTask {
    @Autowired
    SystemOptionsUtil systemOptionsUtil;

    @Scheduled(cron = "0/60 * * * * *")
    public void reloadSystemOptions() {
        systemOptionsUtil.init();
    }
}
