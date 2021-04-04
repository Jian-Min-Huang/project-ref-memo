package org.yfr.finance.core.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item implements Serializable {

    private static final long serialVersionUID = -873275181952102299L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ITEM")
    @SequenceGenerator(sequenceName = "SEQ_ITEM_ID", allocationSize = 1, name = "SEQ_ITEM")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Short type;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column
    private Double openPrice;

    @Column
    private Double highestPrice;

    @Column
    private Double lowestPrice;

    @Column
    private Double closePrice;

}
