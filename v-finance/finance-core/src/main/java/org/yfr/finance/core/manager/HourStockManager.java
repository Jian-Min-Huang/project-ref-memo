package org.yfr.finance.core.manager;


import org.yfr.finance.core.pojo.vo.Stock;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

public enum HourStockManager {

    INSTANCE;

    private List<Stock> stocks = null;

    public void init(List<Stock> minuteStocks) {
        stocks = minuteStocks.stream()
                .filter(v -> timeFileter(v.getDateTime()))
                .map(v -> {
                    if (v.getDateTime().getHour() == 13 && v.getDateTime().getMinute() == 30) {
                        LocalDateTime dateTime = v.getDateTime();
                        v.setDateTime(LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), 14, 0, 0));
                    }

                    return v;
                })
                .collect(toList());
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    private boolean timeFileter(LocalDateTime dateTime) {
        return (dateTime.getHour() == 10 && dateTime.getMinute() == 00) ||
                (dateTime.getHour() == 11 && dateTime.getMinute() == 00) ||
                (dateTime.getHour() == 12 && dateTime.getMinute() == 00) ||
                (dateTime.getHour() == 13 && dateTime.getMinute() == 00) ||
                (dateTime.getHour() == 13 && dateTime.getMinute() == 30);
    }

}
