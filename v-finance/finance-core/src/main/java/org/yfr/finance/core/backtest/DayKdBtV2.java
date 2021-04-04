package org.yfr.finance.core.backtest;

import lombok.extern.slf4j.Slf4j;
import org.yfr.finance.core.pojo.vo.Action;
import org.yfr.finance.core.pojo.vo.Stock;
import org.yfr.finance.core.pojo.vo.indicator.KD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class DayKdBtV2 {

    public static List<Action> highLowStrategy(List<Stock> stocks, List<KD> kds) {
        List<Action> actions = new ArrayList<>();

        boolean flag1 = false, flag2 = false, flag3 = false, flag4 = false;
        int idx = 1;
        double bThres = 70.0, sThres = 40.0;
        for (int i = 2761; i < stocks.size(); i++) {
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

            double m = kds.get(i).getD() - kds.get(i - 1).getD();

            Integer nowStatus = actions.stream()
                    .map(Action::getAmount)
                    .reduce((sum, value) -> sum + value)
                    .orElse(0);

            // 如果向下穿越70, 要將倉位保持到-1
            if (action == 2) {
                for (int j = 0; j <= Math.abs(nowStatus); j++) {
                    actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), -1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                }
                flag1 = false;
                flag2 = false;
                flag3 = false;
                flag4 = false;
                continue;
            }
            // 如果向上穿越40, 要將倉位保持到+1
            if (action == 4 && !flag2) {
                for (int j = 0; j <= Math.abs(nowStatus); j++) {
                    actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), 1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                }
                flag1 = false;
                flag2 = false;
                flag3 = false;
                flag4 = false;
                continue;
            }

            if (flag1) {
                if (kds.get(i).getK() > 80.0) {
                    actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), -1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                }
            }
            if (flag2) {
                if (stocks.get(i).getClosePrice() - actions.get(actions.size() - 1).getPrice() < -150) {
                    actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), -1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                }
            }
            if (flag3) {
                if (kds.get(i).getK() < 30.0) {
                    actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), 1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                }
            }
            if (flag4) {
                if (actions.get(actions.size() - 1).getPrice() - stocks.get(i).getClosePrice() < -150) {
                    actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), 1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                }
            }

            if (nowStatus == 1 && action == 1) {
                // D /, flag1
                // D -, 平
                // D \, 平
                if (m > 0) {
                    if (kds.get(i).getK() > 80.0) {
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), -1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                    } else {
                        flag1 = true;
                    }
                } else {
                    actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), -1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                }
                continue;
            }
            if (nowStatus == 1 && action == 3) {
                // D /, flag2
                // D -, 平
                // D \, 平
                if (m > 0) {
                    if (stocks.get(i).getClosePrice() - actions.get(actions.size() - 1).getPrice() < -150) {
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), -1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                    } else {
                        flag2 = true;
                    }
                } else {
                    actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), -1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                }
                continue;
            }

            if (nowStatus == -1 && action == 3) {
                // D /, 平
                // D -, 平
                // D \, flag3
                if (m > 0) {
                    actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), 1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                } else {
                    if (kds.get(i).getK() < 30.0) {
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), 1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                    } else {
                        flag3 = true;
                    }
                }
                continue;
            }
            if (nowStatus == -1 && action == 1) {
                // D /, 平
                // D -, 平
                // D \, flag4
                if (m > 0) {
                    actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), 1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                } else {
                    if (actions.get(actions.size() - 1).getPrice() - stocks.get(i).getClosePrice() < -150) {
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), 1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                    } else {
                        flag4 = true;
                    }
                }
                continue;
            }
        }

        return actions;
    }

}
