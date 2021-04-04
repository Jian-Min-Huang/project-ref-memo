package org.yfr.finance.core.pojo.vo.indicator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yfr.finance.core.pojo.vo.Stock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KD implements Indicator {

    private LocalDateTime dateTime;

    private Double rsv;

    private Double k;

    private Double d;

    /**
     * List<KD> kds = KD.calculateKD(StockManager.INSTANCE.getStocks(), 9, 3);
     *
     * @param stocks
     * @param parameters
     * @return
     */
    public static List<KD> calculateKD(List<Stock> stocks, int... parameters) {
        assert parameters.length == 2;

        int nDay = parameters[0]; // 9
        int alpha = parameters[1]; // 3

        List<KD> kds = new ArrayList<>();
        for (int i = 0; i < nDay - 2; i++) kds.add(new KD(stocks.get(i).getDateTime(), 0.0, 0.0, 0.0));

        kds.add(KD.builder()
                .dateTime(stocks.get(nDay - 2).getDateTime())
                .rsv(0.0)
                .k(50.0)
                .d(50.0)
                .build()); // 7

        for (int i = nDay - 1; i < stocks.size(); i++) { // 8
            Double max = stocks.subList(i - (nDay - 1), i + 1) // 0 ~ 8
                    .stream()
                    .mapToDouble(Stock::getHighestPrice)
                    .max()
                    .getAsDouble();

            Double min = stocks.subList(i - (nDay - 1), i + 1) // 0 ~ 8
                    .stream()
                    .mapToDouble(Stock::getLowestPrice)
                    .min()
                    .getAsDouble();

            Double rsv = 100.0 * (stocks.get(i).getClosePrice() - min) / (max - min);

            Double alphaR = 1.0 / alpha;
            Double oneMinusAlphaR = 1 - alphaR;

            Double k = (rsv * alphaR) + (kds.get(i - 1).getK() * oneMinusAlphaR);
            Double d = (k * alphaR) + (kds.get(i - 1).getD() * oneMinusAlphaR);

            kds.add(KD.builder()
                    .dateTime(stocks.get(i).getDateTime())
                    .rsv(rsv)
                    .k(k)
                    .d(d)
                    .build());
        }

        return kds;
    }

}
