package org.yfr.finance.core.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawItem implements Serializable {

    private static final long serialVersionUID = -2948124498094391968L;

    private String value;

}
