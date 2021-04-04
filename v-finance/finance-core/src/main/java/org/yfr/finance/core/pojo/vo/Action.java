package org.yfr.finance.core.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.yfr.finance.core.pojo.vo.indicator.KLineType;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class Action {

    private Integer i;

    private Integer idx;

    private LocalDateTime dateTime;

    private Integer amount;

    private String type;

    private Double price;

    private Double profit;

    private Stock stock;

    private KLineType kLineType;

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat(".#");

        return String.format("%d,\t%d,\t%s,\t%d,\t%s,\t%s,\t%s", i, idx, dateTime, amount, type, df.format(price), df.format(profit));
    }

}
