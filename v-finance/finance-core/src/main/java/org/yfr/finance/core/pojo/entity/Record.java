package org.yfr.finance.core.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

//@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Record implements Serializable {

    private static final long serialVersionUID = -6324214309336226005L;

//    @Id
    private String name;

//    @Column(nullable = false)
    private Short status;

//    @Column(nullable = false)
    private LocalDateTime createTime;

}
