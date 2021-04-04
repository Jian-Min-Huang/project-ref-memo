package org.yfr.finance.core.backtest;

import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;
import org.yfr.finance.core.manager.HourStockManager;
import org.yfr.finance.core.manager.MinuteStockManager;
import org.yfr.finance.core.manager.PerformanceManager;
import org.yfr.finance.core.pojo.vo.Action;
import org.yfr.finance.core.pojo.vo.PResult;
import org.yfr.finance.core.pojo.vo.Result;
import org.yfr.finance.core.pojo.vo.indicator.MA;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class HourMaBtTest {

    List<MA> ma20 = MA.calculateMA(HourStockManager.INSTANCE.getStocks(), 20);

    @BeforeClass
    public static void beforeClass() throws Exception {
        MinuteStockManager.INSTANCE.init(true, "TSEA0.csv");
        HourStockManager.INSTANCE.init(MinuteStockManager.INSTANCE.getStocks());
    }

    @Test
    public void upStrategyTest() {
        List<PResult> list = new ArrayList<>();
        for (double thres = 1.0; thres < 100.0; thres++) {
            List<Action> actions = HourMaBt.upStrategy(HourStockManager.INSTANCE.getStocks(), ma20, thres);

            Result result = PerformanceManager.INSTANCE.calResult(actions, HourStockManager.INSTANCE.getStocks().size(), 1);

            list.add(new PResult(thres, 0, result));
        }

        System.out.println(String.format("OSC\tWIN\tEXP\tPERIOD"));
        list.stream()
//                .filter(v -> v.getResult().getWinRate() > 0.80 || v.getResult().getLoseCount() == 0)
//                .filter(v -> v.getResult().getExpValue() > 4.0 || v.getResult().getLoseCount() == 0) // max * 0.6
                .sorted(Comparator.comparing(PResult::getOscBound))
//                .sorted(Comparator.comparing(v -> v.getResult().getExpValue()))
//                .forEach(System.out::println);
                .forEach(v -> System.out.println(String.format("%f\t%f\t%f\t%f", v.getOscBound(), v.getResult().getWinRate(), v.getResult().getExpValue(), v.getResult().getAvgTradingPeriod())));

//        List<Action> actions = HourMaBt.upStrategy(HourStockManager.INSTANCE.getStocks(), ma20, 7.0);
//
//        Result result = PerformanceManager.INSTANCE.calResult(actions, HourStockManager.INSTANCE.getStocks().size(), 1);
//        log.info("\n" + result.toString());
//        log.info("\n" + PerformanceManager.INSTANCE.printAction(actions, result));
//        PerformanceManager.INSTANCE.calStatistic(actions, 1).stream()
//                .forEach(v -> log.info(v.toString()));
    }

    @Test
    public void downStrategyTest() {
        List<PResult> list = new ArrayList<>();
        for (double thres = 1.0; thres < 100.0; thres++) {
            List<Action> actions = HourMaBt.downStrategy(HourStockManager.INSTANCE.getStocks(), ma20, thres);

            Result result = PerformanceManager.INSTANCE.calResult(actions, HourStockManager.INSTANCE.getStocks().size(), 1);

            list.add(new PResult(thres, 0, result));
        }

        System.out.println(String.format("OSC\tWIN\tEXP\tPERIOD"));
        list.stream()
//                .filter(v -> v.getResult().getWinAvg() > Math.abs(v.getResult().getLoseAvg()))
//                .filter(v -> v.getResult().getWinRate() > 0.6)
//                .filter(v -> v.getResult().getExpValue() > 7.0) // max * 0.6
                .sorted(Comparator.comparing(PResult::getOscBound))
//                .forEach(System.out::println);
                .forEach(v -> System.out.println(String.format("%f\t%f\t%f\t%f", v.getOscBound(), v.getResult().getWinRate(), v.getResult().getExpValue(), v.getResult().getAvgTradingPeriod())));

//        List<Action> actions = HourMaBt.downStrategy(HourStockManager.INSTANCE.getStocks(), ma20, 10.0);
//
//        Result result = PerformanceManager.INSTANCE.calResult(actions, HourStockManager.INSTANCE.getStocks().size(), 1);
//        log.info("\n" + result.toString());
//        log.info("\n" + PerformanceManager.INSTANCE.printAction(actions, result));
//        PerformanceManager.INSTANCE.calStatistic(actions, 1).stream()
//                .forEach(v -> log.info(v.toString()));
    }

}
