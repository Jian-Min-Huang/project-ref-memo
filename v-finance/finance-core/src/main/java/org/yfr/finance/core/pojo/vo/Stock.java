package org.yfr.finance.core.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    private LocalDateTime dateTime;

    private Double openPrice;

    private Double highestPrice;

    private Double lowestPrice;

    private Double closePrice;

}
