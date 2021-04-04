package org.yfr.finance.core.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Output {

    private Integer idx;

    private LocalDateTime dateTime;

    private Integer amount;

    private Double price;

    private Double profit;

}
