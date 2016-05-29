package com.pirrigation.water;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.pirrigation.scheduler.Sleeper;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;

import static org.mockito.Mockito.*;

/**
 * Created by r4dx on 03.05.2016.
 */
public class PiPumpTest {

    private final Pin pin = RaspiPin.GPIO_25;
    private GpioPinDigitalOutput outputPin;
    private PiPump pump;
    private Sleeper sleeper;

    @Before
    public void setup() {
        outputPin = mock(GpioPinDigitalOutput.class);
        GpioController gpioController = mock(GpioController.class);
        when(gpioController.provisionDigitalOutputPin(pin)).thenReturn(outputPin);
        sleeper = mock(Sleeper.class);

        pump = new PiPump(gpioController, pin, sleeper);
    }

    @Test
    public void testStarted() {
        final Duration sleepTime = Duration.ofMillis(1);
        pump.start(sleepTime);
        verify(outputPin).high();
        verify(sleeper).sleep(sleepTime.toMillis());
        verify(outputPin).low();
    }

    @Test(expected = RuntimeException.class)
    public void testSetLowOnException() {
        doThrow(new RuntimeException()).when(sleeper).sleep(anyLong());

        final Duration sleepTime = Duration.ofMillis(1);
        pump.start(sleepTime);
        verify(outputPin).high();
        verify(sleeper).sleep(sleepTime.toMillis());
        verify(outputPin).low();
    }
}
