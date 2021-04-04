package org.yfr.finance.core.component.manager;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
@Component
public class ConfigManager {

    @Value("${config.url}")
    private String url;

    private Config config;

    @PostConstruct
    public void postConstruct() throws MalformedURLException {
        try {
            config = ConfigFactory.parseURL(new URL(url));
        } catch (ConfigException ex) {
            log.error(ex.getMessage(), ex);

            config = ConfigFactory.load();
        }
    }

    public void reload(String url) throws MalformedURLException {
        try {
            config = ConfigFactory.parseURL(new URL(url));
        } catch (ConfigException ex) {
            log.error(ex.getMessage(), ex);

            config = ConfigFactory.load();
        }
    }

    public Config getConfig() {
        return config;
    }

}
