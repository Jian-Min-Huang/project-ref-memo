package org.yfr.finance.core.backtest;

import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;
import org.yfr.finance.core.manager.PerformanceManager;
import org.yfr.finance.core.manager.StockManager;
import org.yfr.finance.core.pojo.vo.Action;
import org.yfr.finance.core.pojo.vo.Result;
import org.yfr.finance.core.pojo.vo.indicator.KD;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DayKdBtTest {

    List<KD> kds = KD.calculateKD(StockManager.INSTANCE.getStocks(), 9, 3);

    @BeforeClass
    public static void beforeClass() throws Exception {
        StockManager.INSTANCE.init(true, "Data4.csv");
    }

    @Test
    public void highLowStrategyTest() {
        List<Action> actions = DayKdBt.highLowStrategy(StockManager.INSTANCE.getStocks(), kds, 70.0, 40.0, 150);
        Result result = PerformanceManager.INSTANCE.calResult(actions, StockManager.INSTANCE.getStocks().size() - 2761, 1);

        System.out.println(result.toString());

//        for (int i = 0; i < actions.size(); i++) {
//            System.out.println(actions.get(i).toString() + ", \tk - 1 =" + kds.get(actions.get(i).getI() - 1).getK() + ", \tk =" + kds.get(actions.get(i).getI()).getK());
//        }

        try {
            List<Double> profits = new ArrayList<>();
            for (int i = 0; i < actions.size(); i++) {
                double sum = 0;
                for (int j = 0; j < i + 1; j++) {
                    sum += actions.get(j).getProfit();
                }
                profits.add(sum);
            }

            FileOutputStream fos = new FileOutputStream("data.js");

            StringBuilder builder = new StringBuilder("var dataArr = [");
            actions.forEach(v -> builder.append("{x:").append(v.getIdx()).append(",").append("y:").append(profits.get(v.getIdx() - 1)).append("},"));
            builder.deleteCharAt(builder.length() - 1);
            builder.append("];");

//            fos.write(builder.toString().getBytes());
//            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
