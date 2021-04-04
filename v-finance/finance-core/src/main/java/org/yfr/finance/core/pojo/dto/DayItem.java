package org.yfr.finance.core.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yfr.finance.core.pojo.entity.Item;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayItem implements Serializable {

    private static final long serialVersionUID = -3508509618632293890L;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private String name;

    private String date;

    private String openPrice;

    private String highestPrice;

    private String lowestPrice;

    private String closePrice;

    public Item toItemEntity() {
        return Item.builder()
                .name(name)
                .type(Short.parseShort("4"))
                .dateTime(LocalDate.parse(date, formatter).atStartOfDay().plusMinutes(810))
                .openPrice(Double.parseDouble(openPrice))
                .highestPrice(Double.parseDouble(highestPrice))
                .lowestPrice(Double.parseDouble(lowestPrice))
                .closePrice(Double.parseDouble(closePrice))
                .build();
    }

}
