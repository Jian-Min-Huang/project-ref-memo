package org.yfr.finance.core.backtest;

import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;
import org.yfr.finance.core.manager.HourStockManager;
import org.yfr.finance.core.manager.MinuteStockManager;
import org.yfr.finance.core.manager.PerformanceManager;
import org.yfr.finance.core.pojo.vo.Action;
import org.yfr.finance.core.pojo.vo.Result;
import org.yfr.finance.core.pojo.vo.indicator.KD;

import java.util.List;

@Slf4j
public class HourKdBtTest {

    List<KD> kds = KD.calculateKD(HourStockManager.INSTANCE.getStocks(), 9, 3);

    @BeforeClass
    public static void beforeClass() throws Exception {
        MinuteStockManager.INSTANCE.init(true, "TSEA0.csv");
        HourStockManager.INSTANCE.init(MinuteStockManager.INSTANCE.getStocks());
    }

    @Test
    public void highLowStrategyTest() {
        List<Action> actions = HourKdBt.highLowStrategy(HourStockManager.INSTANCE.getStocks(), kds, 70.0, 30.0);
        Result result = PerformanceManager.INSTANCE.calResult(actions, HourStockManager.INSTANCE.getStocks().size(), 1);

        System.out.println(result.toString());

        for (int i = 0; i < actions.size(); i++) {
            if (actions.get(i).getI() == 0) {
                System.out.println(actions.get(i).toString() + ", \tk - 1 = null, \tk =" + kds.get(actions.get(i).getI()).getK());
            } else {
                System.out.println(actions.get(i).toString() + ", \tk - 1 =" + kds.get(actions.get(i).getI() - 1).getK() + ", \tk =" + kds.get(actions.get(i).getI()).getK());
            }
        }
    }

}
