package org.yfr.finance.core.pojo.enu;

import java.time.LocalDateTime;

public enum Event {

    FIRST(null, 0.0),
    LAST(null, 0.0),

    UP(null, 0.0),
    DOWN(null, 0.0),

    KD_UPWARD_HIGH(null, 0.0),
    KD_DOWNWARD_HIGH(null, 0.0),
    KD_UPWARD_LOW(null, 0.0),
    KD_DOWNWARD_LOW(null, 0.0),

    SHORT_MA_UPWARD_LONG_MA(null, 0.0),
    SHORT_MA_DOWNWARD_LONG_MA(null, 0.0),

    MA_UPWARD_20MA(null, 0.0),
    MA_DOWNWARD_20MA(null, 0.0);

    private LocalDateTime dateTime;
    private Double price;

    Event(LocalDateTime dateTime, Double price) {
        this.dateTime = dateTime;
        this.price = price;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}
