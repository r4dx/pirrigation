package com.pirrigation.factory;

import com.pirrigation.PirrigationService;
import com.pirrigation.config.PumpConfig;
import com.pirrigation.config.SchedulerConfig;
import com.pirrigation.event.Event;
import com.pirrigation.water.Pump;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.function.Supplier;

/**
 * Created by r4dx on 11.06.2016.
 */
@Configuration
public class PirrigationServiceFactory {
    @Bean
    @Scope("singleton")
    public PirrigationService pirrigationService(SchedulerConfig schedulerConfig,
                                                        PumpConfig pumpConfig,
                                                        Pump pump,
                                                        Supplier<Event> eventSupplier) {
        return new PirrigationService(schedulerConfig, pumpConfig, pump, eventSupplier);
    }
}
