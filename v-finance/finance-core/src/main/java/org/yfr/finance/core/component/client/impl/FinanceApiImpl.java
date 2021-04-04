package org.yfr.finance.core.component.client.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.yfr.finance.core.component.client.FinanceApi;
import org.yfr.finance.core.pojo.dto.OperationResponse;

import javax.annotation.Resource;

@Service
@PropertySources({
        @PropertySource(value = "classpath:/api/finance-api.properties"),
        @PropertySource(value = "file:/config/finance-api.properties", ignoreResourceNotFound = true)})
public class FinanceApiImpl implements FinanceApi {

    @Value("#{'${apiServerUrl}' + '${infoUrl}' + '${fetchAmericaIdxUrl}'}")
    private String fetchAmericaIdx;

    @Resource
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<OperationResponse> fetchAmericaIdx() {
        return restTemplate.exchange(fetchAmericaIdx, HttpMethod.GET, null, new ParameterizedTypeReference<OperationResponse>() {
        });
    }

}
