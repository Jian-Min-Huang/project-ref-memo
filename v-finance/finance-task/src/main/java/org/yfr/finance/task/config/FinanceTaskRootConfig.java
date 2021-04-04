package org.yfr.finance.task.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan(basePackages = "org.yfr.finance.core.component")
@EnableScheduling
public class FinanceTaskRootConfig {
}
