package org.yfr.finance.core.component.client;

import org.springframework.http.ResponseEntity;
import org.yfr.finance.core.pojo.dto.OperationResponse;

public interface FinanceApi {

    ResponseEntity<OperationResponse> fetchAmericaIdx();

}
