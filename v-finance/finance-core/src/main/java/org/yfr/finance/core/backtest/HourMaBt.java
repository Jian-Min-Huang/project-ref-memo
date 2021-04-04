package org.yfr.finance.core.backtest;

import lombok.extern.slf4j.Slf4j;
import org.yfr.finance.core.pojo.vo.Action;
import org.yfr.finance.core.pojo.vo.Stock;
import org.yfr.finance.core.pojo.vo.indicator.MA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
public class HourMaBt {

    public static List<Action> upStrategy(List<Stock> stocks, List<MA> mas, double thres) {
        List<Action> actions = new ArrayList<>();
        List<Integer> distribution = Arrays.asList(0, 0, 0, 0, 0, 0);

        int idx = 1;
        for (int i = 0; i < stocks.size(); i++) {
            if (mas.get(i).getMa() == null ||
                    mas.get(i - 1).getMa() == null ||
                    mas.get(i - 2).getMa() == null ||
                    mas.get(i - 3).getMa() == null) continue;

            Integer nowStatus = actions.stream()
                    .map(Action::getAmount)
                    .reduce((sum, value) -> sum + value)
                    .orElse(0);

            // - +
            // - + +
            // - + + +
            int t4 = (mas.get(i - 3).getMa() > stocks.get(i - 3).getClosePrice()) ? -1 : 1;
            int t3 = (mas.get(i - 2).getMa() > stocks.get(i - 2).getClosePrice()) ? -1 : 1;
            int t2 = (mas.get(i - 1).getMa() > stocks.get(i - 1).getClosePrice()) ? -1 : 1;
            int t1 = (mas.get(i).getMa() > stocks.get(i).getClosePrice()) ? -1 : 1;

            // 建新多倉
            if (nowStatus == 0) {
                if (t2 == -1 && t1 == 1) {
                    if (upFilter(stocks.get(i).getClosePrice() - mas.get(i).getMa(), thres, i, actions)) {
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), 1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        distribution.set(0, distribution.get(0) + 1);
                        continue;
                    }
                }
                if (t3 == -1 && t2 == 1 && t1 == 1) {
                    if (upFilter(stocks.get(i).getClosePrice() - mas.get(i).getMa(), thres, i - 1, actions)) {
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), 1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        distribution.set(0, distribution.get(0) + 1);
                        continue;
                    }
                }
                if (t4 == -1 && t3 == 1 && t2 == 1 && t1 == 1) {
                    if (upFilter(stocks.get(i).getClosePrice() - mas.get(i).getMa(), thres, i - 2, actions)) {
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), 1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        distribution.set(0, distribution.get(0) + 1);
                        continue;
                    }
                }
            }

            // 比20MA低則平倉
            if (nowStatus == 1 && stocks.get(i).getClosePrice() < mas.get(i).getMa()) {
                actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), -1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                distribution.set(1, distribution.get(1) + 1);
                continue;
            }

            // 比買進價高則平倉
            if (nowStatus == 1 && stocks.get(i).getClosePrice() > actions.get(actions.size() - 1).getPrice()) {
                List<Double> count = new ArrayList<>();
                for (int j = actions.get(actions.size() - 1).getI(); j < i; j++) {
                    count.add(stocks.get(i).getClosePrice() - stocks.get(j).getClosePrice());
                }
                long countSum = count.stream().filter(v -> v > 0).count();

                if (countSum > 2) {
                    actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), -1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                    distribution.set(2, distribution.get(2) + 1);
                    continue;
                }
            }
        }

//        log.info("{}", distribution.toString());

        return actions;
    }

    public static List<Action> downStrategy(List<Stock> stocks, List<MA> mas, double thres) {
        List<Action> actions = new ArrayList<>();
        List<Integer> distribution = Arrays.asList(0, 0, 0, 0, 0, 0);

        int idx = 1;
        for (int i = 0; i < stocks.size(); i++) {
            if (mas.get(i).getMa() == null ||
                    mas.get(i - 1).getMa() == null ||
                    mas.get(i - 2).getMa() == null ||
                    mas.get(i - 3).getMa() == null) continue;

            Integer nowStatus = actions.stream()
                    .map(Action::getAmount)
                    .reduce((sum, value) -> sum + value)
                    .orElse(0);

            // + -
            // + - -
            // + - - -
            int t4 = (mas.get(i - 3).getMa() > stocks.get(i - 3).getClosePrice()) ? -1 : 1;
            int t3 = (mas.get(i - 2).getMa() > stocks.get(i - 2).getClosePrice()) ? -1 : 1;
            int t2 = (mas.get(i - 1).getMa() > stocks.get(i - 1).getClosePrice()) ? -1 : 1;
            int t1 = (mas.get(i).getMa() > stocks.get(i).getClosePrice()) ? -1 : 1;

            // 建新空倉
            if (nowStatus == 0) {
                if (t2 == 1 && t1 == -1) {
                    if (downFilter(mas.get(i).getMa() - stocks.get(i).getClosePrice(), thres, i, actions)) {
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), -1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        distribution.set(3, distribution.get(3) + 1);
                        continue;
                    }
                }
                if (t3 == 1 && t2 == -1 && t1 == -1) {
                    if (downFilter(mas.get(i).getMa() - stocks.get(i).getClosePrice(), thres, i - 1, actions)) {
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), -1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        distribution.set(3, distribution.get(3) + 1);
                        continue;
                    }
                }
                if (t4 == 1 && t3 == -1 && t2 == -1 && t1 == -1) {
                    if (downFilter(mas.get(i).getMa() - stocks.get(i).getClosePrice(), thres, i - 2, actions)) {
                        actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), -1, "S", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                        distribution.set(3, distribution.get(3) + 1);
                        continue;
                    }
                }
            }

            // 比20MA高則平倉
            if (nowStatus == -1 && stocks.get(i).getClosePrice() > mas.get(i).getMa()) {
                actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), 1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                distribution.set(4, distribution.get(4) + 1);
                continue;
            }

            // 比賣出價低則平倉
            if (nowStatus == -1 && actions.get(actions.size() - 1).getPrice() > stocks.get(i).getClosePrice()) {
                List<Double> count = new ArrayList<>();
                for (int j = actions.get(actions.size() - 1).getI(); j < i; j++) {
                    count.add(stocks.get(i).getClosePrice() - stocks.get(j).getClosePrice());
                }
                long countSum = count.stream().filter(v -> v < 0).count();

                if (countSum > 1) {
                    actions.add(new Action(i, idx++, stocks.get(i).getDateTime(), 1, "B", stocks.get(i).getClosePrice(), 0.0, stocks.get(i), null));
                    distribution.set(5, distribution.get(5) + 1);
                    continue;
                }
            }
        }

//        log.info("{}", distribution.toString());

        return actions;
    }

    private static boolean upFilter(double dif, double bound, int crossI, List<Action> actions) {
        if (dif < bound) {
            return false;
        }

        List<Integer> collect = actions.stream().map(Action::getI).collect(toList());
        return !collect.contains(crossI);
    }

    private static boolean downFilter(double dif, double bound, int crossI, List<Action> actions) {
        if (dif < bound) {
            return false;
        }

        List<Integer> collect = actions.stream().map(Action::getI).collect(toList());
        return !collect.contains(crossI);
    }

}
