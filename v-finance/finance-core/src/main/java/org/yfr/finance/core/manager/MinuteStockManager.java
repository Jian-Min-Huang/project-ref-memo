package org.yfr.finance.core.manager;


import org.yfr.finance.core.pojo.vo.Stock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public enum MinuteStockManager {

    INSTANCE;

    private List<Stock> stocks = new ArrayList<>();

    public void init(Boolean inClassPath, String sourceFilePath) throws Exception {
        BufferedReader reader = null;
        if (inClassPath) {
            reader = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(sourceFilePath)));
        } else {
            reader = new BufferedReader(new FileReader(sourceFilePath));
        }

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

        String content;
        while ((content = reader.readLine()) != null) {
            String[] split = content.split(",");
            Stock stock = Stock.builder()
                    .dateTime(LocalDateTime.parse(split[1], df))
                    .openPrice(Double.parseDouble(split[2]))
                    .highestPrice(Double.parseDouble(split[3]))
                    .lowestPrice(Double.parseDouble(split[4]))
                    .closePrice(Double.parseDouble(split[5]))
                    .build();
            stocks.add(stock);
        }
    }

    public List<Stock> getStocks() {
        return stocks;
    }

}
