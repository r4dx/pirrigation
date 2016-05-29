package com.pirrigation.providers;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pirrigation.config.PumpConfig;
import com.pirrigation.config.SchedulerConfig;
import com.pirrigation.scheduler.Sleeper;
import com.pirrigation.water.Pump;
import com.typesafe.config.Config;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.Duration;
import java.util.function.Supplier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    public void testProvidePirrigationServiceDoesNotThrowExceptions() {
        SchedulerConfig config = mock(SchedulerConfig.class);
        when(config.getCheckFrequency()).thenReturn(Duration.ofMillis(1));
        module.providePirrigationService(config, mock(PumpConfig.class), mock(Pump.class), mock(Supplier.class));
    }

    @Test
    public void testProvidePumpDoesNotThrowExceptions() {
        module.providePump(mock(Sleeper.class), mock(PumpConfig.class), mock(GpioController.class));
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
