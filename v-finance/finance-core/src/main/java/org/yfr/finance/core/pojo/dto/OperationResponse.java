package org.yfr.finance.core.pojo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationResponse implements Serializable {

    @JsonIgnore
    private static final long serialVersionUID = -2258028512920197797L;

    @JsonIgnore
    public static final OperationResponse SUCCESS_RESPONSE = new OperationResponse(1, "SUCCESS!");

    @JsonIgnore
    private static final Integer DEFAULT_SUCCESS_CODE = 1;

    private Integer code = DEFAULT_SUCCESS_CODE;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

}
