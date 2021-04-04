package org.yfr.finance.core.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PResult {

    double oscBound;

    int shift;

    Result result;

}
