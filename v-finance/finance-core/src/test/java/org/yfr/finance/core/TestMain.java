package org.yfr.finance.core;

import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;
import org.yfr.finance.core.manager.HourStockManager;
import org.yfr.finance.core.manager.MinuteStockManager;
import org.yfr.finance.core.manager.StockManager;

@Slf4j
public class TestMain {

    @BeforeClass
    public static void beforeClass() throws Exception {
        MinuteStockManager.INSTANCE.init(true, "TSEA0.csv");
        HourStockManager.INSTANCE.init(MinuteStockManager.INSTANCE.getStocks());
        StockManager.INSTANCE.init(true, "TSEA4.csv");
    }

    @Test
    public void test() {
//        HourEventMonitor.INSTANCE.start();
    }

}
