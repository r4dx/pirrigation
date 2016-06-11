package com.pirrigation.water;

import com.pi4j.io.gpio.*;
import com.pirrigation.scheduler.Sleeper;

import java.time.Duration;

/**
 * Created by r4dx on 01.05.2016.
 */
public class PiPump implements Pump {

    private volatile boolean processing;

    private final Sleeper sleeper;
    private final GpioPinDigitalOutput pumpPin;

    public PiPump(GpioController gpioController, Pin pin, Sleeper sleeper) {
        this.sleeper = sleeper;
        pumpPin = gpioController.provisionDigitalOutputPin(pin);
    }

    @Override
    public void start(Duration workDuration) {
        if (processing)
            throw new IllegalStateException("Already pumping");

        processing = true;
        pumpPin.high();
        try {
            sleeper.sleep(workDuration.toMillis());
        }
        finally {
            pumpPin.low();
            processing = false;
        }
    }
}
