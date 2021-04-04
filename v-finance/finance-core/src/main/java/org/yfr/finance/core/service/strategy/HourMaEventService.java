package org.yfr.finance.core.service.strategy;

import org.springframework.stereotype.Component;
import org.yfr.finance.core.component.HourMaStrategy;
import org.yfr.finance.core.component.StrategyMachine;
import org.yfr.finance.core.pojo.dto.Output;
import org.yfr.finance.core.pojo.enu.Event;
import org.yfr.finance.core.pojo.vo.Stock;
import org.yfr.finance.core.pojo.vo.indicator.MA;
import org.yfr.finance.core.service.OutputService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class HourMaEventService {

    @Resource
    private OutputService outputService;

    public void start(List<Stock> stocks) {
        /* calculate 20 ma . */
        List<MA> ma20 = MA.calculateMA(stocks, 20);

        /* new HourMaStrategy . */
        StrategyMachine strategyMachine = new HourMaStrategy();

        /* find event and put return output into outputs . */
        List<Optional<Output>> outputs = new ArrayList<>();
        for (int i = 0; i < ma20.size(); i++) {
            if (i == 0) {
                Event e = Event.FIRST;
                e.setDateTime(stocks.get(i).getDateTime());
                strategyMachine.input(e);
            }
            if (i == ma20.size() - 1) {
                Event e = Event.LAST;
                e.setDateTime(stocks.get(i).getDateTime());
                strategyMachine.input(e);
            }

            if (i < 20) {
                outputs.add(Optional.empty());
                continue;
            }

            if (stocks.get(i).getClosePrice() > ma20.get(i).getMa() &&
                    stocks.get(i - 1).getClosePrice() < ma20.get(i - 1).getMa()) {
                Event e = Event.MA_UPWARD_20MA;
                e.setDateTime(stocks.get(i).getDateTime());
                e.setPrice(stocks.get(i).getClosePrice());

                outputs.add(strategyMachine.input(e));
                continue;
            }

            if (stocks.get(i).getClosePrice() < ma20.get(i).getMa() &&
                    stocks.get(i - 1).getClosePrice() > ma20.get(i - 1).getMa()) {
                Event e = Event.MA_DOWNWARD_20MA;
                e.setDateTime(stocks.get(i).getDateTime());
                e.setPrice(stocks.get(i).getClosePrice());

                outputs.add(strategyMachine.input(e));
                continue;
            }

            if (stocks.get(i).getClosePrice() > stocks.get(i - 1).getClosePrice()) {
                Event e = Event.UP;
                e.setDateTime(stocks.get(i).getDateTime());
                e.setPrice(stocks.get(i).getClosePrice());

                outputs.add(strategyMachine.input(e));
                continue;
            }

            if (stocks.get(i).getClosePrice() < stocks.get(i - 1).getClosePrice()) {
                Event e = Event.DOWN;
                e.setDateTime(stocks.get(i).getDateTime());
                e.setPrice(stocks.get(i).getClosePrice());

                outputs.add(strategyMachine.input(e));
                continue;
            }

            outputs.add(Optional.empty());
        }

        /* invoke summation . */
        System.out.println(strategyMachine.summation());

        /* generate data.js */
        outputService.generateDataJs(50, stocks, ma20, outputs);
    }

}
