package com.pirrigation;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pirrigation.config.PirrigationServiceConfig;
import com.pirrigation.config.PirrigationServiceConfigStubImpl;
import com.pirrigation.scheduler.Sleeper;
import com.pirrigation.water.Pump;
import com.pirrigation.water.StubPump;

/**
 * Created by r4dx on 09.05.2016.
 */
public class PirrigationModule extends AbstractModule {
    @Override
    protected void configure() {
    }

    @Provides
    private PirrigationServiceConfig providePirrigationServiceConfig() {
        return new PirrigationServiceConfigStubImpl();
    }

    @Provides
    private PirrigationService providePirrigationService(PirrigationServiceConfig config, Pump pump) {
        return new PirrigationService(config, pump);
    }

    @Provides
    private Pump providePump(Sleeper sleeper, PirrigationServiceConfig config/*, GpioController controller*/) {
        return new StubPump(sleeper);
        //return new PiPump(controller, RaspiPin.getPinByName(config.getPumpControlPin()), sleeper);
    }

    @Provides
    private GpioController provideGpioController() {
        return GpioFactory.getInstance();
    }

    @Provides
    private Sleeper provideSleeper() {
        return Sleeper.DEFAULT;
    }
}
