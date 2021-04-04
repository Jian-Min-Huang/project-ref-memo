package org.yfr.finance.core.pojo.vo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Statistic {

    private int year;

    private Integer tradingCount;

    private Double tradingPerformance;

}
