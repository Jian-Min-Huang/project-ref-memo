package org.yfr.finance.core.backtest;

import lombok.extern.slf4j.Slf4j;
import org.yfr.finance.core.pojo.vo.Action;
import org.yfr.finance.core.pojo.vo.Stock;
import org.yfr.finance.core.pojo.vo.indicator.KD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class HourKdBt {

    public static List<Action> highLowStrategy(List<Stock> stocks, List<KD> kds, double bThres, double sThres) {
        List<Action> actions = new ArrayList<>();
        List<Integer> distribution = Arrays.asList(0, 0, 0, 0, 0, 0);

        int idx = 1;
        // default
        // 70 < x      , +1
        // 30 < x <= 70,  0
        // x <= 30     , -1
        if (kds.get(0).getK() <= sThres) {
            actions.add(new Action(0, idx++, stocks.get(0).getDateTime(), -1, "S", stocks.get(0).getClosePrice(), 0.0, stocks.get(0), null));
        } else if (kds.get(0).getK() > bThres) {
            actions.add(new Action(0, idx++, stocks.get(0).getDateTime(), 1, "B", stocks.get(0).getClosePrice(), 0.0, stocks.get(0), null));
        }
        for (int i = 1; i < stocks.size(); i++) {
            Integer nowStatus = actions.stream()
                    .map(Action::getAmount)
                    .reduce((sum, value) -> sum + value)
                    .orElse(0);

            // A, up cross k70
            // B, down cross k70
            // C, up cross k30
            // D, down cross k30

            // +1 + A = x
            // +1 + B = -1 -1
            // +1 + C = x
            // +1 + D = -1 -1

            // 0 + A = +1
            // 0 + B = -1
            // 0 + C = +1
            // 0 + D = -1

            // -1 + A = +1 +1
            // -1 + B = x
            // -1 + C = +1 +1
            // -1 + D = x

            int action = 0;
            if (kds.get(i - 1).getK() < bThres && kds.get(i).getK() > bThres) {
                action = 1;
            }
            if (kds.get(i - 1).getK() > bThres && kds.get(i).getK() < bThres) {
                action = 2;
            }
            if (kds.get(i - 1).getK() < sThres && kds.get(i).getK() > sThres) {
                action = 3;
            }
            if (kds.get(i - 1).getK() > sThres && kds.get(i).getK() < sThres) {
                action = 4;
            }

            if (nowStatus == 1) {
                switch (action) {
                    case 2:
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), -1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), -1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        break;
                    case 4:
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), -1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), -1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        break;
                }
                continue;
            }

            if (nowStatus == 0) {
                switch (action) {
                    case 1:
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), 1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        break;
                    case 2:
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), -1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        break;
                    case 3:
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), 1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        break;
                    case 4:
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), -1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        break;
                }
                continue;
            }

            if (nowStatus == -1) {
                switch (action) {
                    case 1:
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), 1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), 1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        break;
                    case 3:
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), 1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), 1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        break;
                }
                continue;
            }
        }

//        log.info("{}", distribution.toString());

        return actions;
    }

}
