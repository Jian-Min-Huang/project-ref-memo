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
public class MACD implements Indicator {

    private LocalDateTime dateTime;

    private Double di;

    private Double ema1;

    private Double ema2;

    private Double dif;

    private Double macd;

    private Double osc;

    /**
     * List<MACD> macds = MACD.calculateMACD(StockManager.INSTANCE.getStocks(),12, 26);
     *
     * @param stocks
     * @param parameters
     * @return
     */
    public static List<MACD> calculateMACD(List<Stock> stocks, int... parameters) {
        assert parameters.length == 2;

        int nDay1 = parameters[0]; // 12
        int nDay2 = parameters[1]; // 26

        List<MACD> macds = new ArrayList<>();
        stocks.forEach(value -> macds.add(MACD.builder().dateTime(value.getDateTime()).di((value.getHighestPrice() + value.getLowestPrice() + (2 * value.getClosePrice())) / 4).build()));

        macds.subList(0, nDay1) // 0~11
                .stream()
                .map(MACD::getDi)
                .reduce((sum, value) -> sum + value)
                .ifPresent(value -> macds.get(nDay1 - 1).setEma1(value / nDay1)); // 11, 12

        macds.subList(0, nDay2) // 0~25
                .stream()
                .map(MACD::getDi)
                .reduce((sum, value) -> sum + value)
                .ifPresent(value -> macds.get(nDay2 - 1).setEma2(value / nDay2)); // 25, 26

        for (int i = nDay1; i < stocks.size(); i++) { // 12
            double ema1 = (macds.get(i - 1).getEma1() * (double) (nDay1 - 1) / (double) (nDay1 + 1)) + (macds.get(i).getDi() * 2 / (double) (nDay1 + 1));
            macds.get(i).setEma1(ema1);
        }

        for (int i = nDay2; i < stocks.size(); i++) { // 26
            double ema2 = (macds.get(i - 1).getEma2() * (double) (nDay2 - 1) / (double) (nDay2 + 1)) + (macds.get(i).getDi() * 2 / (double) (nDay2 + 1));
            macds.get(i).setEma2(ema2);
        }

        for (int i = nDay2 - 1; i < stocks.size(); i++) { // 25
            macds.get(i).setDif(macds.get(i).getEma1() - macds.get(i).getEma2());
        }

        macds.subList(nDay2 - 1, (nDay2 - 1) + 9) // 25~33
                .stream()
                .map(MACD::getDif)
                .reduce((sum, value) -> sum + value)
                .ifPresent(value -> macds.get((nDay2 - 2) + 9).setMacd(value / 9)); // 33

        for (int i = (nDay2 - 1) + 9; i < stocks.size(); i++) {
            Double macd = (macds.get(i - 1).getMacd() * 8 / 10) + (macds.get(i).getDif() * 2 / 10);
            macds.get(i).setMacd(macd);
        }

        for (int i = (nDay2 - 2) + 9; i < stocks.size(); i++) {
            macds.get(i).setOsc(macds.get(i).getDif() - macds.get(i).getMacd());
        }

        return macds;
    }

}
