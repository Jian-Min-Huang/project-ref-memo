package org.yfr.finance.core.pojo.vo.indicator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.yfr.finance.core.pojo.vo.Stock;

@NoArgsConstructor
@Getter
@Setter
public class KLineType {

    // 顏色
    private String color;

    // 上影
    private Integer shadowU;

    // 實體
    private Integer entityL;

    // 下影
    private Integer shadowD;

    public static KLineType produce(Stock stock) {
        KLineType kLineType = new KLineType();

        if (stock.getOpenPrice() > stock.getClosePrice()) {
            kLineType.setColor("G");
            kLineType.setShadowU(new Double(stock.getHighestPrice() - stock.getOpenPrice()).intValue());
            kLineType.setEntityL(new Double(stock.getOpenPrice() - stock.getClosePrice()).intValue());
            kLineType.setShadowD(new Double(stock.getClosePrice() - stock.getLowestPrice()).intValue());
        } else {
            kLineType.setColor("R");
            kLineType.setShadowU(new Double(stock.getHighestPrice() - stock.getClosePrice()).intValue());
            kLineType.setEntityL(new Double(stock.getClosePrice() - stock.getOpenPrice()).intValue());
            kLineType.setShadowD(new Double(stock.getOpenPrice() - stock.getLowestPrice()).intValue());
        }

        return kLineType;
    }

    @Override
    public String toString() {
        return String.format("KLineType(%s,%d,%d,%d", color, shadowU, entityL, shadowD);
    }
}
