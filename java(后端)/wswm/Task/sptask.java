package wswm.Task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import wswm.service.appusr;
import wswm.utils.WebSocketServer;

@Slf4j
@Component
public class sptask {
    @Autowired
    appusr appusr;

    @Scheduled(fixedRate = 300000)
    public void task1() {
        appusr.statuschg2();
        appusr.statuschg();
        log.info("已间隔5分钟,订单状态已更新");
    }

    @Scheduled(fixedRate = 40000)
    public void task2() {
        WebSocketServer.broadcast("ping");
        log.info("已间隔40秒发送ping");
    }
}
