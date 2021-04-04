package org.yfr.finance.core.backtest;

import lombok.extern.slf4j.Slf4j;
import org.yfr.finance.core.pojo.vo.Action;
import org.yfr.finance.core.pojo.vo.Stock;
import org.yfr.finance.core.pojo.vo.indicator.KD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class DayKdBt {

    public static List<Action> highLowStrategy(List<Stock> stocks, List<KD> kds, double bThres, double sThres, double bound) {
        List<Action> actions = new ArrayList<>();
        List<Integer> distribution = Arrays.asList(0, 0, 0, 0, 0, 0);

        int idx = 1;
        for (int i = 2761; i < stocks.size(); i++) {
            Integer nowStatus = actions.stream()
                    .map(Action::getAmount)
                    .reduce((sum, value) -> sum + value)
                    .orElse(0);

            if (nowStatus == 1) {
                if (stocks.get(i).getClosePrice() - actions.get(actions.size() - 1).getPrice() < -1 * bound) {
                    actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), -1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                    continue;
                }
            } else if (nowStatus == -1) {
                if (actions.get(actions.size() - 1).getPrice() - stocks.get(i).getClosePrice() < -1 * bound) {
                    actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), 1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                    continue;
                }
            }

            // A, up cross k70
            // B, down cross k70
            // C, down cross k30
            // D, up cross k30

            int action = 0;
            if (kds.get(i - 1).getK() < bThres && kds.get(i).getK() > bThres) {
                action = 1;
            }
            if (kds.get(i - 1).getK() > bThres && kds.get(i).getK() < bThres) {
                action = 2;
            }
            if (kds.get(i - 1).getK() > sThres && kds.get(i).getK() < sThres) {
                action = 3;
            }
            if (kds.get(i - 1).getK() < sThres && kds.get(i).getK() > sThres) {
                action = 4;
            }

            // 0, up cross 72 x
            // 0, down cross 72 -1
            // 0, down cross 28 x
            // 0, up cross 28 +1

            // 1, up cross 72 -1
            // 1, down cross 72 x
            // 1, down cross 28 -1
            // 1, up cross 28 x

            // -1, up cross 72 +1
            // -1, down cross x
            // -1, down cross +1
            // -1, up cross 28 x

            if (nowStatus == 0) {
                switch (action) {
                    case 1:
                        break;
                    case 2:
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), -1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        break;
                    case 3:
                        break;
                    case 4:
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), 1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        break;
                }
                continue;
            }

            if (nowStatus == 1) {
                switch (action) {
                    case 1:
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), -1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        break;
                    case 2:
                        break;
                    case 3:
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), -1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        break;
                    case 4:
                        throw new RuntimeException();
                }
                continue;
            }

            if (nowStatus == -1) {
                switch (action) {
                    case 1:
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), 1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        break;
                    case 2:
                        throw new RuntimeException();
                    case 3:
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), 1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        break;
                    case 4:
                        throw new RuntimeException();
                }
                continue;
            }
        }

        return actions;
    }

}
