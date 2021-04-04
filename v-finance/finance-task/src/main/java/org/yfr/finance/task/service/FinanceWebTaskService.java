package org.yfr.finance.task.service;

public interface FinanceWebTaskService {

    void sendHeartBeat();

    void sendDumpMinKCommand();

    void sendDumpDayKCommand();

    void importFile2DB();

    void fetchAmericaIdx();

}
