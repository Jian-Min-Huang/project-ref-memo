package org.yfr.finance.core.manager;


import org.yfr.finance.core.pojo.vo.Stock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public enum StockManager {

    INSTANCE;

    private List<Stock> stocks = new ArrayList<>();

    public void init(Boolean inClassPath, String sourceFilePath) throws Exception {
        BufferedReader reader = null;
        if (inClassPath) {
            reader = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(sourceFilePath)));
        } else {
            reader = new BufferedReader(new FileReader(sourceFilePath));
        }

        String content;
        while ((content = reader.readLine()) != null) {
            String[] split = content.split(",");
            String[] dateStrSplit = split[1].split("/");
            LocalDate date = LocalDate.of(Integer.parseInt(dateStrSplit[0]), Integer.parseInt(dateStrSplit[1]), Integer.parseInt(dateStrSplit[2]));
            Stock stock = Stock.builder()
                    .dateTime(date.atStartOfDay())
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
