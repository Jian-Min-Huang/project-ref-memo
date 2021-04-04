package org.yfr.finance.core.manager;


import org.yfr.finance.core.pojo.vo.Action;
import org.yfr.finance.core.pojo.vo.Result;
import org.yfr.finance.core.pojo.vo.Statistic;
import org.yfr.finance.core.pojo.vo.indicator.KLineType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public enum PerformanceManager {

    INSTANCE;

    public Result calResult(List<Action> actions, int tradindDateCount, int nThFilter) {
        calProfit(actions);

        List<Action> sortedAction = actions.stream()
                .sorted(Comparator.comparing(Action::getProfit))
                .collect(toList());

        Result result = new Result();
        result.setProfitHBound((sortedAction.size() <= nThFilter) ? 0 : sortedAction.get(sortedAction.size() - nThFilter).getProfit());
        result.setProfitLBound((sortedAction.size() <= nThFilter) ? 0 : sortedAction.get(nThFilter - 1).getProfit());
        result.setWinCount(actions.stream()
                .filter(v -> v.getProfit() > 0)
                .filter(v -> v.getProfit() < result.getProfitHBound())
                .count());
        result.setLoseCount(actions.stream()
                .filter(v -> v.getProfit() < 0)
                .filter(v -> v.getProfit() > result.getProfitLBound())
                .count());
        result.setWinSum(actions.stream()
                .filter(v -> v.getProfit() > 0)
                .filter(v -> v.getProfit() < result.getProfitHBound())
                .mapToDouble(Action::getProfit)
                .sum());
        result.setLoseSum(actions.stream()
                .filter(v -> v.getProfit() < 0)
                .filter(v -> v.getProfit() > result.getProfitLBound())
                .mapToDouble(Action::getProfit)
                .sum());
        result.setExpValue((result.getWinCount() + result.getLoseCount() == 0) ? 0 : (result.getWinSum() + result.getLoseSum()) / (result.getWinCount() + result.getLoseCount()));
        result.setAvgTradingPeriod(tradindDateCount / (actions.size() / 2.0));

        return result;
    }

    public List<Statistic> calStatistic(List<Action> actions, int nThFilter) {
        calProfit(actions);

        List<Action> sortedAction = actions.stream()
                .sorted(Comparator.comparing(Action::getProfit))
                .collect(toList());

        double hBound = (sortedAction.size() <= nThFilter) ? 0.0 : sortedAction.get(sortedAction.size() - nThFilter).getProfit();
        double lBound = (sortedAction.size() <= nThFilter) ? 0.0 : sortedAction.get(nThFilter - 1).getProfit();

        Map<Integer, List<Action>> yearActionMap = actions.stream()
                .collect(groupingBy(v -> v.getDateTime().getYear()));

        List<Statistic> statistics = new ArrayList<>();
        yearActionMap.keySet()
                .stream()
                .sorted(Comparator.naturalOrder())
                .map(yearActionMap::get)
                .map(v -> v.stream().filter(v1 -> v1.getProfit() < hBound && v1.getProfit() > lBound).collect(toList()))
                .forEach(v -> {
                    long count = v.stream()
                            .filter(v1 -> !v1.getProfit().equals(0.0))
                            .count();

                    double sum = v.stream()
                            .mapToDouble(Action::getProfit)
                            .sum();

                    statistics.add(Statistic.builder()
                            .year(v.get(0).getDateTime().getYear())
                            .tradingCount((int) count / 2)
                            .tradingPerformance(sum)
                            .build());
                });

        return statistics;
    }

    public String printAction(List<Action> actions, Result result) {
        StringBuilder sb = new StringBuilder();
        actions.stream()
                .map(v -> {
                    if (v.getProfit() < result.getProfitLBound() || v.getProfit() > result.getProfitHBound())
                        v.setProfit(0.0);

                    return v;
                })
                .forEach(v -> sb.append(v.toString()).append("\n"));

        return sb.toString();
    }

    private void calProfit(List<Action> actions) {
        for (int i = 0; i < actions.size(); i += 2) {
            if (i + 1 == actions.size()) continue;

            if (actions.get(i + 1).getType().equals("S")) {
                actions.get(i + 1).setProfit(actions.get(i + 1).getPrice() - actions.get(i).getPrice());
            } else {
                actions.get(i + 1).setProfit(actions.get(i).getPrice() - actions.get(i + 1).getPrice());
            }
//            actions.get(i).setKLineType(KLineType.produce(actions.get(i).getStock()));
        }
    }

}
