package org.yfr.finance.core.service;

import org.yfr.finance.core.pojo.dto.OperationResponse;
import org.yfr.finance.core.pojo.vo.Stock;

import java.util.List;

public interface FinanceItemService {

    List<Stock> findHourTSEA(Short sortType);

    List<Stock> findDayTSEA(Short sortType);

    OperationResponse saveMinuteItems() throws Exception;

    OperationResponse saveDayItems() throws Exception;

}
