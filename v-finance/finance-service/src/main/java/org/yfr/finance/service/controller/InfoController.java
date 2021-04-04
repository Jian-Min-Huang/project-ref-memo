package org.yfr.finance.service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yfr.finance.core.pojo.dto.OperationResponse;
import org.yfr.finance.service.service.CrawlerService;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping(value = "${infoUrl}")
public class InfoController {

    @Resource
    private CrawlerService crawlerService;

    @GetMapping(value = "${fetchAmericaIdxUrl}")
    OperationResponse fetchAmericaIdx() {
        return crawlerService.fetchAmericaIdx();
    }

}
