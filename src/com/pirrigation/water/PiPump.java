package com.pirrigation.water;

import com.pi4j.io.gpio.*;

/**
 * Created by r4dx on 01.05.2016.
 */
public class PiPump implements Pump {

    private final GpioController gpioController;
    private final Pin pin;

    public PiPump(GpioController gpioController, Pin pin) {
        this.gpioController = gpioController;
        this.pin = pin;
    }

    @Override
    public void start(long workSeconds) {
        GpioPinDigitalOutput pumpPin = gpioController.provisionDigitalOutputPin(pin);
        pumpPin.high();
        try {
            Thread.sleep(workSeconds * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        pumpPin.low();
    }
}
