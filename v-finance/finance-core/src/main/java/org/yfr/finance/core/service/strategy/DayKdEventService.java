package org.yfr.finance.core.service.strategy;

import org.springframework.stereotype.Component;
import org.yfr.finance.core.component.HourMaStrategy;
import org.yfr.finance.core.component.StrategyMachine;
import org.yfr.finance.core.component.strategy.DayKdStrategy;
import org.yfr.finance.core.pojo.dto.Output;
import org.yfr.finance.core.pojo.enu.Event;
import org.yfr.finance.core.pojo.vo.Stock;
import org.yfr.finance.core.pojo.vo.indicator.KD;
import org.yfr.finance.core.pojo.vo.indicator.MA;
import org.yfr.finance.core.service.OutputService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DayKdEventService {

    public void start(List<Stock> stocks) {
        /* calculate kd . */
        List<KD> kds = KD.calculateKD(stocks, 9, 3);

        /* new HourMaStrategy . */
        StrategyMachine strategyMachine = new DayKdStrategy();

        /* find event and put return output into outputs . */
        List<Optional<Output>> outputs = new ArrayList<>();
        for (int i = 0; i < kds.size(); i++) {
            if (i == 0) {
                Event e = Event.FIRST;
                e.setDateTime(stocks.get(i).getDateTime());
                strategyMachine.input(e);
            }
            if (i == kds.size() - 1) {
                Event e = Event.LAST;
                e.setDateTime(stocks.get(i).getDateTime());
                strategyMachine.input(e);
            }

            if (i < 9) {
                outputs.add(Optional.empty());
                continue;
            }

            if (kds.get(i).getK() > 40 && kds.get(i - 1).getK() < 40) {
                Event e = Event.KD_UPWARD_LOW;
                e.setDateTime(stocks.get(i).getDateTime());
                e.setPrice(stocks.get(i).getClosePrice());

                outputs.add(strategyMachine.input(e));
                continue;
            }

            if (kds.get(i).getK() > 70 && kds.get(i - 1).getK() < 70) {
                Event e = Event.KD_UPWARD_HIGH;
                e.setDateTime(stocks.get(i).getDateTime());
                e.setPrice(stocks.get(i).getClosePrice());

                outputs.add(strategyMachine.input(e));
                continue;
            }

            if (kds.get(i).getK() < 70 && kds.get(i - 1).getK() > 70) {
                Event e = Event.KD_DOWNWARD_HIGH;
                e.setDateTime(stocks.get(i).getDateTime());
                e.setPrice(stocks.get(i).getClosePrice());

                outputs.add(strategyMachine.input(e));
                continue;
            }

            if (kds.get(i).getK() < 40 && kds.get(i - 1).getK() > 40) {
                Event e = Event.KD_DOWNWARD_LOW;
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
//        outputService.generateDataJs(50, stocks, ma20, outputs);
    }

}
