package org.yfr.finance.core.controller;

import org.springframework.web.bind.annotation.*;
import org.yfr.finance.core.component.manager.ConfigManager;
import org.yfr.finance.core.pojo.dto.ConfigDto;
import org.yfr.finance.core.pojo.dto.OperationResponse;

import javax.annotation.Resource;
import java.net.MalformedURLException;

@RestController
@RequestMapping(path = "/config")
public class ConfigController {

    @Resource
    private ConfigManager configManager;

    @GetMapping(path = "/printAll")
    public String printAll() {
        return configManager.getConfig().root().render();
    }

    @PostMapping(path = "/reload")
    public OperationResponse reload(@RequestBody ConfigDto configDto) throws MalformedURLException {
        configManager.reload(configDto.getUrl());

        return OperationResponse.SUCCESS_RESPONSE;
    }

}
