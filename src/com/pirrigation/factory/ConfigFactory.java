package com.pirrigation.factory;

import com.pirrigation.config.PumpConfig;
import com.pirrigation.config.PumpConfigImpl;
import com.pirrigation.config.SchedulerConfig;
import com.pirrigation.config.SchedulerConfigImpl;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.File;

/**
 * Created by r4dx on 11.06.2016.
 */
@Configuration
public class ConfigFactory {
    private static final Logger logger = LoggerFactory.getLogger(ConfigFactory.class);

    @Bean
    @Scope("singleton")
    public PumpConfig pumpConfig(Config hoconConfig) {
        PumpConfig result = new PumpConfigImpl(hoconConfig);
        logger.info("Loaded pumpConfig: " + result.toString());
        return result;
    }

    @Bean
    @Scope("singleton")
    public SchedulerConfig schedulerConfig(Config hoconConfig) {
        SchedulerConfig result = new SchedulerConfigImpl(hoconConfig);
        logger.info("Loaded schedulerConfig: " + result.toString());
        return result;
    }

    @Bean
    @Scope("singleton")
    public Config hoconConfig() {
        return com.typesafe.config.ConfigFactory.parseFile(new File("conf/pirrigation.conf"));
    }
}
