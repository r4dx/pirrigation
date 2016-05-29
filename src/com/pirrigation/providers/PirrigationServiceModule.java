package com.pirrigation.providers;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;
import com.pirrigation.PirrigationService;
import com.pirrigation.config.PirrigationServiceConfig;
import com.pirrigation.config.PirrigationServiceConfigStubImpl;
import com.pirrigation.event.Event;
import com.pirrigation.scheduler.Sleeper;
import com.pirrigation.water.PiPump;
import com.pirrigation.water.Pump;
import com.pirrigation.water.StubPump;
import org.mockito.Mockito;

import java.util.function.Supplier;

/**
 * Created by r4dx on 09.05.2016.
 */
public class PirrigationServiceModule extends AbstractModule {
    @Override
    protected void configure() {
    }

    @Provides
    public PirrigationServiceConfig providePirrigationServiceConfig() {
        return new PirrigationServiceConfigStubImpl();
    }

    @Provides
    public PirrigationService providePirrigationService(PirrigationServiceConfig config, Pump pump,
                                                        Supplier<Event> eventSupplier) {
        return new PirrigationService(config, pump, eventSupplier);
    }

    @Provides
    public Pump providePump(Sleeper sleeper, PirrigationServiceConfig config, GpioController controller) {
        return new StubPump(sleeper);
        //return new PiPump(controller, config.getPumpControlPin(), sleeper);
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
