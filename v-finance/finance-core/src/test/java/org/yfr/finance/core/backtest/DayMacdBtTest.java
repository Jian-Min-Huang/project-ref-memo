package org.yfr.finance.core.backtest;

import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;
import org.yfr.finance.core.manager.PerformanceManager;
import org.yfr.finance.core.manager.StockManager;
import org.yfr.finance.core.pojo.vo.Action;
import org.yfr.finance.core.pojo.vo.PResult;
import org.yfr.finance.core.pojo.vo.Result;
import org.yfr.finance.core.pojo.vo.indicator.MACD;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class DayMacdBtTest {

    List<MACD> macds = MACD.calculateMACD(StockManager.INSTANCE.getStocks(),12, 26);

    @BeforeClass
    public static void beforeClass() throws Exception {
        StockManager.INSTANCE.init(true, "TSEA4.csv");
    }

    // find lose point and why lose it 價指標 量指標 KD, ...
    @Test
    public void upStrategyTest() {
        List<PResult> list = new ArrayList<>();
        for (double oscBound = 1.0; oscBound < 30; oscBound++) {
            for (int shift = 1; shift < 10; shift++) {
                List<Action> actions = DayMacdBt.upStrategy(
                        StockManager.INSTANCE.getStocks(),
                        macds,
                        oscBound,
                        shift);

                Result result = PerformanceManager.INSTANCE.calResult(actions, StockManager.INSTANCE.getStocks().size() - 2761, 5);

                list.add(new PResult(oscBound, shift, result));
            }
        }

        list.stream()
                .filter(v -> v.getResult().getWinAvg() > Math.abs(v.getResult().getLoseAvg()))
                .filter(v -> v.getResult().getWinRate() > 0.5)
                .filter(v -> v.getResult().getExpValue() > 24.0) // max * 0.6
//                .sorted(Comparator.comparing(PResult::getOscBound).thenComparing(PResult::getShift))
                .sorted(Comparator.comparing(v -> v.getResult().getExpValue()))
//                .forEach(System.out::println);
                .forEach(v -> System.out.println(String.format("%f\t%d\t%f\t%f", v.getOscBound(), v.getShift(), v.getResult().getExpValue(), v.getResult().getAvgTradingPeriod())));

//        System.out.println("======");
//        List<Action> actions = DayMacdBt.upStrategy(
//                StockManager.INSTANCE.getStocks(),
//                macds,
//                5.0,
//                3);
//
//        Result result = PerformanceManager.INSTANCE.calResult(actions, StockManager.INSTANCE.getStocks().size() - 2761, 5);
//        System.out.println(result.toString());
//
//        actions.forEach(System.out::println);
//
//        try {
//            List<Double> profits = new ArrayList<>();
//            for (int i = 0; i < actions.size(); i++) {
//                double sum = 0;
//                for (int j = 0; j < i + 1; j++) {
//                    sum += actions.get(j).getProfit();
//                }
//                profits.add(sum);
//            }
//
//            FileOutputStream fos = new FileOutputStream("output.csv");
//
//            StringBuilder builder = new StringBuilder();
//            actions.forEach(v -> builder.append(v.getIdx()).append(",").append(profits.get(v.getIdx() - 1)).append("\n"));
//
//            fos.write(builder.toString().getBytes());
//            fos.flush();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void downStrategyTest() {
        List<PResult> list = new ArrayList<>();
        for (double oscBound = 1.0; oscBound < 30; oscBound++) {
            for (int shift = 1; shift < 10; shift++) {
                List<Action> actions = DayMacdBt.downStrategy(
                        StockManager.INSTANCE.getStocks(),
                        macds,
                        oscBound,
                        shift);

                Result result = PerformanceManager.INSTANCE.calResult(actions, StockManager.INSTANCE.getStocks().size() - 2761, 5);

                list.add(new PResult(oscBound, shift, result));
            }
        }

        list.stream()
                .filter(v -> v.getResult().getWinAvg() > Math.abs(v.getResult().getLoseAvg()))
                .filter(v -> v.getResult().getWinRate() > 0.5)
                .filter(v -> v.getResult().getExpValue() > 12.0) // max * 0.6
                .sorted(Comparator.comparing(PResult::getOscBound).thenComparing(PResult::getShift))
//                .forEach(System.out::println);
                .forEach(v -> System.out.println(String.format("%f\t%d\t%f\t%f", v.getOscBound(), v.getShift(), v.getResult().getExpValue(), v.getResult().getAvgTradingPeriod())));

//        System.out.println("======");
//        List<Action> actions = DayMacdBt.downStrategy(
//                StockManager.INSTANCE.getStocks(),
//                macds,
//                1.0,
//                4);
//
//        Result result = PerformanceManager.INSTANCE.calResult(actions, StockManager.INSTANCE.getStocks().size() - 2761, 5);
//        System.out.println(result.toString());
//
//        actions.forEach(System.out::println);
//
//        try {
//            List<Double> profits = new ArrayList<>();
//            for (int i = 0; i < actions.size(); i++) {
//                double sum = 0;
//                for (int j = 0; j < i + 1; j++) {
//                    sum += actions.get(j).getProfit();
//                }
//                profits.add(sum);
//            }
//
//            FileOutputStream fos = new FileOutputStream("output2.csv");
//
//            StringBuilder builder = new StringBuilder();
//            actions.forEach(v -> builder.append(v.getIdx()).append(",").append(profits.get(v.getIdx() - 1)).append("\n"));
//
//            fos.write(builder.toString().getBytes());
//            fos.flush();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
