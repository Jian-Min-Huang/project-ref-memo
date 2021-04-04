package org.yfr.finance.core.pojo.vo.indicator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yfr.finance.core.pojo.vo.Stock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PSY implements Indicator {

    private LocalDateTime dateTime;

    private Double period1;

    private Double period2;

    /**
     * List<PSY> psys = PSY.calculatePSY(StockManager.INSTANCE.getStocks(), 12, 24);
     *
     * @param stocks
     * @param parameters
     * @return
     */
    public static List<PSY> calculatePSY(List<Stock> stocks, int... parameters) {
        assert parameters.length == 2;

        int nDay1 = parameters[0]; // 12
        int nDay2 = parameters[1]; // 24

        List<Integer> upDownPointList = new ArrayList<>(Arrays.asList(0));
        for (int i = 1; i < stocks.size(); i++) {
            if (stocks.get(i).getClosePrice() - stocks.get(i - 1).getClosePrice() > 0) {
                upDownPointList.add(1);
            } else {
                upDownPointList.add(0);
            }
        }

        List<PSY> psys = new ArrayList<>();
        for (int i = nDay2; i <= stocks.size(); i++) {
            long nDay1UpCount = upDownPointList.subList(i - nDay1, i).stream()
                    .mapToInt(value -> value)
                    .sum();

            long nDay2UpCount = upDownPointList.subList(i - nDay2, i).stream()
                    .mapToInt(value -> value)
                    .sum();

            psys.add(PSY.builder()
                    .dateTime(stocks.get(i - 1).getDateTime())
                    .period1((double) nDay1UpCount / (double) nDay1 * 100.0)
                    .period2((double) nDay2UpCount / (double) nDay2 * 100.0)
                    .build());
        }

        return psys;
    }

}
