package com.pirrigation.factory;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pirrigation.config.PumpConfig;
import com.pirrigation.scheduler.Sleeper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.mock;

/**
 * Created by r4dx on 11.06.2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({GpioFactory.class})
public class PumpFactoryTest {
    private final PumpFactory factory = new PumpFactory();

    @Test
    public void testPumpDoesNotThrowExceptions() {
        factory.pump(mock(Sleeper.class), mock(PumpConfig.class), mock(GpioController.class));
    }

    @Test
    public void testGpioControllerDoesNotThrowExceptions() {
        // Actually GpioFactory throws exception on PC so we'll mock it
        PowerMockito.mockStatic(GpioFactory.class);
        factory.gpioController();
    }

    @Test
    public void testSleeperDoesNotThrowExceptions() {
        factory.sleeper();
    }
}
