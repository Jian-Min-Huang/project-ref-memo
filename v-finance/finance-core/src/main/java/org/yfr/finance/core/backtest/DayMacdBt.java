package org.yfr.finance.core.backtest;

import lombok.extern.slf4j.Slf4j;
import org.yfr.finance.core.pojo.vo.Action;
import org.yfr.finance.core.pojo.vo.Stock;
import org.yfr.finance.core.pojo.vo.indicator.MACD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
public class DayMacdBt {

    public static List<Action> upStrategy(List<Stock> stocks, List<MACD> macds, Double oscBound, int shift) {
        List<Action> actions = new ArrayList<>();
        List<Integer> distribution = Arrays.asList(0, 0, 0, 0, 0, 0);

        int idx = 1;
        for (int i = 2760; i < stocks.size(); i++) {
//            log.info("i {}, date {}, dif {}, macd {}, osc {}", i, macds.get(i).getDateTime(), macds.get(i).getDif(), macds.get(i).getMacd(), macds.get(i).getOsc());

            Integer nowStatus = actions.stream()
                    .map(Action::getAmount)
                    .reduce((sum, value) -> sum + value)
                    .orElse(0);

            // 建新多倉
            if (nowStatus == 0) {
                if (macds.get(i - 1).getOsc() < 0 && macds.get(i).getOsc() > 0) {
                    if (upFilter(i, i, actions, stocks, macds, oscBound)) {
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(),1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        distribution.set(4, distribution.get(4) + 1);
                        continue;
                    }
                }
                if (macds.get(i - 2).getOsc() < 0 && macds.get(i - 1).getOsc() > 0) {
                    if (upFilter(i, i - 1, actions, stocks, macds, oscBound)) {
                        actions.add(new Action(i - 1, idx++, stocks.get(i).getDateTime(),1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        distribution.set(4, distribution.get(4) + 1);
                        continue;
                    }
                }
                if (macds.get(i - 3).getOsc() < 0 && macds.get(i - 2).getOsc() > 0) {
                    if (upFilter(i, i - 2, actions, stocks, macds, oscBound)) {
                        actions.add(new Action(i - 2, idx++, stocks.get(i).getDateTime(),1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        distribution.set(4, distribution.get(4) + 1);
                        continue;
                    }
                }
                if (macds.get(i - 4).getOsc() < 0 && macds.get(i - 3).getOsc() > 0) {
                    if (upFilter(i, i - 3, actions, stocks, macds, oscBound)) {
                        actions.add(new Action(i - 3, idx++, stocks.get(i).getDateTime(),1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        distribution.set(4, distribution.get(4) + 1);
                        continue;
                    }
                }
            }

            // 超過平倉天數
            if (nowStatus == 1 && i - actions.get(actions.size() - 1).getI() > shift) {
                actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), -1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                distribution.set(2, distribution.get(2) + 1);
                continue;
            }
        }

//        log.info("{}", distribution.toString());

        return actions;
    }

    public static List<Action> downStrategy(List<Stock> stocks, List<MACD> macds, Double oscBound, int shift) {
        List<Action> actions = new ArrayList<>();
        List<Integer> distribution = Arrays.asList(0, 0, 0, 0, 0, 0);

        int idx = 1;
        for (int i = 2760; i < stocks.size(); i++) {
//            log.info("i {}, date {}, dif {}, macd {}, osc {}", i, macds.get(i).getDateTime(), macds.get(i).getDif(), macds.get(i).getMacd(), macds.get(i).getOsc());

            Integer nowStatus = actions.stream()
                    .map(Action::getAmount)
                    .reduce((sum, value) -> sum + value)
                    .orElse(0);

            // 建新空倉
            if (nowStatus == 0) {
                if (macds.get(i - 1).getOsc() > 0 && macds.get(i).getOsc() < 0) {
                    if (downFilter(i, i, actions, stocks, macds, oscBound)) {
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(),-1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        distribution.set(5, distribution.get(5) + 1);
                        continue;
                    }
                }
                if (macds.get(i - 2).getOsc() > 0 && macds.get(i - 1).getOsc() < 0) {
                    if (downFilter(i, i - 1, actions, stocks, macds, oscBound)) {
                        actions.add(new Action(i - 1, idx++, stocks.get(i).getDateTime(),-1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        distribution.set(5, distribution.get(5) + 1);
                        continue;
                    }
                }
                if (macds.get(i - 3).getOsc() > 0 && macds.get(i - 2).getOsc() < 0) {
                    if (downFilter(i, i - 2, actions, stocks, macds, oscBound)) {
                        actions.add(new Action(i - 2, idx++, stocks.get(i).getDateTime(),-1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        distribution.set(5, distribution.get(5) + 1);
                        continue;
                    }
                }
                if (macds.get(i - 4).getOsc() > 0 && macds.get(i - 3).getOsc() < 0) {
                    if (downFilter(i, i - 3, actions, stocks, macds, oscBound)) {
                        actions.add(new Action(i - 3, idx++, stocks.get(i).getDateTime(),-1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        distribution.set(5, distribution.get(5) + 1);
                        continue;
                    }
                }
            }

            // 超過平倉天數
            if (nowStatus == -1 && i - actions.get(actions.size() - 1).getI() > shift) {
                actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), 1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                distribution.set(3, distribution.get(3) + 1);
                continue;
            }
        }

//        log.info("{}", distribution.toString());

        return actions;
    }

    private static boolean upFilter(Integer i, Integer crossI, List<Action> actions, List<Stock> stocks, List<MACD> macds, Double oscBound) {
        if (i <= 2760) return false;

        if (macds.get(i).getOsc() < oscBound) {
//            log.debug("osc, {}," + macds.get(i).getOsc());
            return false;
        }

        List<Integer> collect = actions.stream().map(Action::getI).collect(toList());
        return !collect.contains(crossI);
    }

    private static boolean downFilter(Integer i, Integer crossI, List<Action> actions, List<Stock> stocks, List<MACD> macds, Double oscBound) {
        if (i <= 2760) return false;

        if (macds.get(i).getOsc() > -1 * oscBound) {
//            log.debug("osc, {}," + macds.get(i).getOsc());
            return false;
        }

        List<Integer> collect = actions.stream().map(Action::getI).collect(toList());
        return !collect.contains(crossI);
    }

}
