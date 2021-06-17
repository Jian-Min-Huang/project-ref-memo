package org.yfr.finance.core.service.strategy;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.yfr.finance.core.service.FinanceItemService;
import org.yfr.finance.web.config.FinanceWebRootConfig;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FinanceWebRootConfig.class)
public class HourMaEventServiceTest {

    @Resource
    private HourMaEventService hourMaEventService;

    @Resource
    private FinanceItemService financeItemService;

    @Test
    public void contextLoads() {
        hourMaEventService.start(financeItemService.findHourTSEA((short) 1));
    }

}