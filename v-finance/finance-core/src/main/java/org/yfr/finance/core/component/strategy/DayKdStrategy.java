package org.yfr.finance.core.component.strategy;

import lombok.extern.slf4j.Slf4j;
import org.yfr.finance.core.component.StrategyMachine;
import org.yfr.finance.core.pojo.dto.Output;
import org.yfr.finance.core.pojo.enu.Event;
import org.yfr.finance.core.pojo.vo.Summation;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class DayKdStrategy extends StrategyMachine {

    private Integer idx = 1;

    private LocalDateTime first;
    private LocalDateTime last;

    @Override
    public Optional<Output> input(Event event) {
        if (event.equals(Event.FIRST)) {
            first = event.getDateTime();
            return Optional.empty();
        }
        if (event.equals(Event.LAST)) {
            last = event.getDateTime();
            return Optional.empty();
        }

        switch (status) {
            case 1:
                return b1(event);
            case 0:
                return zero(event);
            case -1:
                return s1(event);
            default:
                throw new RuntimeException("not support status !");
        }
    }

    @Override
    public Summation summation() {
        if (outputs.size() % 2 != 0) outputs.removeLast();

        Integer winCount = 0;
        Integer allTradeCount = outputs.size() / 2;
        List<Output> list = (LinkedList<Output>) outputs;
        for (int i = 0; i < outputs.size(); i += 2) {
            double v1 = list.get(i).getAmount() * -1 * list.get(i).getPrice();
            double v2 = list.get(i + 1).getAmount() * -1 * list.get(i + 1).getPrice();
            list.get(i + 1).setProfit(v1 + v2);
            if (v1 + v2 > 0) winCount++;
        }

        outputs.forEach(System.out::println);

        Summation summation = new Summation();
        summation.setWinCount(winCount);
        summation.setAllTradeCount(allTradeCount);
        summation.setWinRate(winCount.doubleValue() / allTradeCount.doubleValue());

        Duration duration = Duration.between(first, last);
        summation.setTradeDuration(duration.toDays());
        summation.setTradeFrequency(duration.toDays() / allTradeCount.doubleValue());

        Double sum = outputs.stream()
                .map(v -> v.getAmount() * -1 * v.getPrice())
                .reduce((s, a) -> s + a)
                .get();

        summation.setWinAccumulation(sum);
        summation.setExpectedValue(sum / allTradeCount.doubleValue());

        return summation;
    }

    private Optional<Output> b1(Event event) {
        switch (event) {
            case KD_UPWARD_HIGH:
            case KD_DOWNWARD_LOW:
                status = 0;
                Output outputS = Output.builder().idx(idx++).dateTime(event.getDateTime()).amount(-1).price(event.getPrice()).build();
                outputs.offer(outputS);
                return Optional.of(outputS);
            default:
                throw new RuntimeException("b1 not support event !");
        }
    }

    private Optional<Output> zero(Event event) {
        switch (event) {
            case KD_UPWARD_LOW:
                status = 1;
                Output outputB = Output.builder().idx(idx++).dateTime(event.getDateTime()).amount(1).price(event.getPrice()).build();
                outputs.offer(outputB);
                return Optional.of(outputB);
            case KD_DOWNWARD_HIGH:
                status = -1;
                Output outputS = Output.builder().idx(idx++).dateTime(event.getDateTime()).amount(-1).price(event.getPrice()).build();
                outputs.offer(outputS);
                return Optional.of(outputS);
            case KD_UPWARD_HIGH:
            case KD_DOWNWARD_LOW:
                return Optional.empty();
            default:
                throw new RuntimeException("zero Not Support Event !");
        }
    }

    private Optional<Output> s1(Event event) {
        switch (event) {
            case KD_UPWARD_HIGH:
            case KD_DOWNWARD_LOW:
                status = 0;
                Output outputB = Output.builder().idx(idx++).dateTime(event.getDateTime()).amount(1).price(event.getPrice()).build();
                outputs.offer(outputB);
                return Optional.of(outputB);
            default:
                throw new RuntimeException("s1 not support event !");
        }
    }

}
