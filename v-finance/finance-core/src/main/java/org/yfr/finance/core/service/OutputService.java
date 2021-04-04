package org.yfr.finance.core.service;

import org.yfr.finance.core.pojo.dto.Output;
import org.yfr.finance.core.pojo.vo.Stock;
import org.yfr.finance.core.pojo.vo.indicator.MA;

import java.util.List;
import java.util.Optional;

public interface OutputService {

    void generateDataJs(Integer batchSize, List<Stock> stocks, List<MA> ma20, List<Optional<Output>> outputs);

}
