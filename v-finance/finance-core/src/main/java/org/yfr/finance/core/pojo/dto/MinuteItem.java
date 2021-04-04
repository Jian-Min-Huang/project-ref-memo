package org.yfr.finance.core.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yfr.finance.core.pojo.entity.Item;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MinuteItem implements Serializable {

    private static final long serialVersionUID = 4910029621111492974L;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

    private String name;

    private String dateTime;

    private String openPrice;

    private String highestPrice;

    private String lowestPrice;

    private String closePrice;

    public Item toItemEntity() {
        return Item.builder()
                .name(name)
                .type(Short.parseShort("0"))
                .dateTime(LocalDateTime.parse(dateTime, formatter))
                .openPrice(Double.parseDouble(openPrice))
                .highestPrice(Double.parseDouble(highestPrice))
                .lowestPrice(Double.parseDouble(lowestPrice))
                .closePrice(Double.parseDouble(closePrice))
                .build();
    }

}
