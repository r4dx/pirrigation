package com.pirrigation.providers;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;
import com.pirrigation.PirrigationService;
import com.pirrigation.config.*;
import com.pirrigation.event.Event;
import com.pirrigation.scheduler.Sleeper;
import com.pirrigation.water.PiPump;
import com.pirrigation.water.Pump;
import com.pirrigation.water.StubPump;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.function.Supplier;

/**
 * Created by r4dx on 09.05.2016.
 */
public class PirrigationServiceModule extends AbstractModule {
    private static final Logger logger = LoggerFactory.getLogger(PirrigationServiceModule.class);

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    public SchedulerConfig provideSchedulerConfig(Config hoconConfig) {
        SchedulerConfig result = new SchedulerConfigImpl(hoconConfig);
        logger.info("Loaded schedulerConfig: " + result.toString());
        return result;
    }

    @Provides
    @Singleton
    public PumpConfig providePumpConfig(Config hoconConfig) {
        PumpConfig result = new PumpConfigImpl(hoconConfig);
        logger.info("Loaded pumpConfig: " + result.toString());
        return result;
    }

    @Provides
    @Singleton
    public Config provideHoconConfig() {
        return ConfigFactory.parseFile(new File("conf/pirrigation.conf"));
    }

    @Provides
    public PirrigationService providePirrigationService(SchedulerConfig schedulerConfig,
                                                        PumpConfig pumpConfig,
                                                        Pump pump,
                                                        Supplier<Event> eventSupplier) {
        return new PirrigationService(schedulerConfig, pumpConfig, pump, eventSupplier);
    }

    @Provides
    public Pump providePump(Sleeper sleeper, PumpConfig config, GpioController controller) {
        return new StubPump(sleeper);
        //return new PiPump(controller, config.getControlPin(), sleeper);
    }

    @Provides
    public GpioController provideGpioController() {
        return Mockito.mock(GpioController.class);
        //return GpioFactory.getInstance();
    }

    @Provides
    public Sleeper provideSleeper() {
        return Sleeper.DEFAULT;
    }
}
