package org.yfr.finance.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.yfr.finance.core.component.bot.TelegramBot;

@Configuration
@ComponentScan(basePackages = "org.yfr.finance.core.component")
@ComponentScan(basePackages = "org.yfr.finance.service")
//@EnableJpaRepositories(basePackages = "org.yfr.finance.core.repository")
//@EntityScan(basePackages = "org.yfr.finance.core.pojo.entity")
public class FinanceServiceRootConfig {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
