package org.yfr.finance.core.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Summation {

    private Integer winCount;

    private Integer allTradeCount;

    private Double winRate;

    private Long tradeDuration;

    private Double tradeFrequency;

    private Double winAccumulation;

    private Double expectedValue;

}
