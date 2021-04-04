package org.yfr.finance.core.pojo.vo.indicator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yfr.finance.core.pojo.vo.Stock;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MA implements Indicator {

    private LocalDateTime dateTime;

    private Double ma;

    /**
     * List<MA> ma5 = MA.calculateMA(StockManager.INSTANCE.getStocks(), 5);
     *
     * @param stocks
     * @param parameters
     * @return
     */
    public static List<MA> calculateMA(List<Stock> stocks, int... parameters) {
        assert parameters.length == 1;

        List<MA> mas = stocks.stream()
                                .map(v -> MA.builder()
                                            .dateTime(v.getDateTime())
                                            .build())
                                .collect(toList());

        int nDay = parameters[0]; // 5

        for (int i = nDay - 1; i < mas.size(); i++) {
            mas.get(i).setMa(stocks.subList(i - nDay + 1, i + 1).stream()
                    .map(Stock::getClosePrice)
                    .reduce((sum, element) -> sum + element)
                    .get() / nDay);
        }

        return mas;
    }

}
