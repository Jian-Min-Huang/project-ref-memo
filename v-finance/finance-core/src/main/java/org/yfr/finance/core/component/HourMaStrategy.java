package org.yfr.finance.core.component;

import lombok.extern.slf4j.Slf4j;
import org.yfr.finance.core.pojo.dto.Output;
import org.yfr.finance.core.pojo.enu.Event;
import org.yfr.finance.core.pojo.vo.Summation;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class HourMaStrategy extends StrategyMachine {

    private static final Integer THRES = 2;
    private static final Integer bsTHRES = 1;

    private Integer count = 0;
    private Integer bCount = 1;
    private Integer sCount = -1;
    private Boolean bGate = false;
    private Boolean sGate = false;

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
            case MA_DOWNWARD_20MA:
                status = 0;
                bCount = 1;
                count = -1;
                sGate = true;
                Output output = Output.builder().idx(idx++).dateTime(event.getDateTime()).amount(-1).price(event.getPrice()).build();
                outputs.offer(output);
                return Optional.of(output);
            case UP:
                bCount++;
                if (bCount > bsTHRES) {
                    status = 0;
                    bCount = 1;
                    Output outputS = Output.builder().idx(idx++).dateTime(event.getDateTime()).amount(-1).price(event.getPrice()).build();
                    outputs.offer(outputS);
                    return Optional.of(outputS);
                }
                return Optional.empty();
            case DOWN:
//                log.debug("b1 skip event !");
                return Optional.empty();
            case MA_UPWARD_20MA:
                throw new RuntimeException("b1 error event !");
            default:
                throw new RuntimeException("b1 not support event !");
        }
    }

    private Optional<Output> zero(Event event) {
        switch (event) {
            case MA_UPWARD_20MA:
                count = 1;
                bGate = true;
                return Optional.empty();
            case UP:
                if (count >= 1) count++;
                if (bGate && count > THRES) {
                    status = 1;
                    bGate = false;
                    Output outputB = Output.builder().idx(idx++).dateTime(event.getDateTime()).amount(1).price(event.getPrice()).build();
                    outputs.offer(outputB);
                    return Optional.of(outputB);
                }
                return Optional.empty();
            case MA_DOWNWARD_20MA:
                count = -1;
                sGate = true;
                return Optional.empty();
            case DOWN:
                if (count <= -1) count--;
                if (sGate && count < -1 * THRES) {
                    status = -1;
                    sGate = false;
                    Output outputS = Output.builder().idx(idx++).dateTime(event.getDateTime()).amount(-1).price(event.getPrice()).build();
                    outputs.offer(outputS);
                    return Optional.of(outputS);
                }
                return Optional.empty();
            default:
                throw new RuntimeException("zero Not Support Event !");
        }
    }

    private Optional<Output> s1(Event event) {
        switch (event) {
            case MA_UPWARD_20MA:
                status = 0;
                sCount = -1;
                count = 1;
                bGate = true;
                Output output = Output.builder().idx(idx++).dateTime(event.getDateTime()).amount(1).price(event.getPrice()).build();
                outputs.offer(output);
                return Optional.of(output);
            case DOWN:
                sCount--;
                if (sCount < -1 * bsTHRES) {
                    status = 0;
                    sCount = -1;
                    Output outputB = Output.builder().idx(idx++).dateTime(event.getDateTime()).amount(1).price(event.getPrice()).build();
                    outputs.offer(outputB);
                    return Optional.of(outputB);
                }
                return Optional.empty();
            case UP:
//                log.debug("s1 skip event !");
                return Optional.empty();
            case MA_DOWNWARD_20MA:
                throw new RuntimeException("s1 error event !");
            default:
                throw new RuntimeException("s1 not support event !");
        }
    }

}
