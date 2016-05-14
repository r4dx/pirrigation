package com.pirrigation.providers;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pirrigation.config.PirrigationServiceConfig;
import com.pirrigation.scheduler.Sleeper;
import com.pirrigation.water.Pump;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.function.Supplier;

import static org.mockito.Mockito.mock;

/**
 * Created by r4dx on 09.05.2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({GpioFactory.class})
public class PirrigationServiceModuleTest {
    private PirrigationServiceModule module = new PirrigationServiceModule();

    @Test
    public void testConfigureDoesNotThrowExceptions() {
        module.configure();
    }

    @Test
    public void testProvidePirrigationServiceConfigDoesNotThrowExceptions() {
        module.providePirrigationServiceConfig();
    }

    @Test
    public void testProvidePirrigationServiceDoesNotThrowExceptions() {
        module.providePirrigationService(mock(PirrigationServiceConfig.class), mock(Pump.class), mock(Supplier.class));
    }

    @Test
    public void testProvidePumpDoesNotThrowExceptions() {
        module.providePump(mock(Sleeper.class), mock(PirrigationServiceConfig.class), mock(GpioController.class));
    }

    @Test
    public void testProvideGpioControllerDoesNotThrowExceptions() {
        // Actually GpioFactory throws exception on PC so we'll mock it
        PowerMockito.mockStatic(GpioFactory.class);
        module.provideGpioController();
    }

    @Test
    public void testProvideSleeperDoesNotThrowExceptions() {
        module.provideSleeper();
    }
}
