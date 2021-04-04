package org.yfr.finance.task.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.yfr.finance.core.component.client.FinanceApi;
import org.yfr.finance.core.component.conn.TcpClient;
import org.yfr.finance.core.pojo.enu.Command;
import org.yfr.finance.core.service.FinanceItemService;
import org.yfr.finance.task.service.FinanceWebTaskService;

import javax.annotation.Resource;

@Slf4j
@Service
public class FinanceWebTaskServiceImpl implements FinanceWebTaskService {

    @Resource
    private TcpClient tcpClient;

//    @Resource
//    private FinanceItemService financeItemService;

    @Resource
    private FinanceApi financeApi;

    @Override
    @Scheduled(cron = "0 0/30 * * * *")
    public void sendHeartBeat() {
        tcpClient.sendCommand(Command.HEART_BEAT.build()).ifPresent(log::info);
    }

    @Override
    @Scheduled(cron = "0 10,40 9,10,11,12,13 * * *")
    public void sendDumpMinKCommand() {
        tcpClient.sendCommand(Command.DUMP_MIN_K.build()).ifPresent(log::info);
    }

    @Override
    @Scheduled(cron = "5 40 13 * * *")
    public void sendDumpDayKCommand() {
        tcpClient.sendCommand(Command.DUMP_DAY_K.build()).ifPresent(log::info);
    }

    @Override
    @Scheduled(cron = "10 0/5 9,10,11,12,13 * * *")
    public void importFile2DB() {
        log.info("importFile2DB");
//        log.info(financeItemService.saveMinuteItems().toString());
//        log.info(financeItemService.saveDayItems().toString());
    }

    @Override
    @Scheduled(cron = "0 0 9 * * *")
    public void fetchAmericaIdx() {
        financeApi.fetchAmericaIdx();
    }

}
