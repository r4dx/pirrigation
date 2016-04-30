package com.pirrigation;

import com.pi4j.io.gpio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PirrigationApp {
    private static final Logger logger = LoggerFactory.getLogger(PirrigationApp.class);

    public static void main(String[] args) {

        final Pin port =  RaspiPin.GPIO_25;
        final int seconds = 5;

        logger.info("Enabling {} for {} seconds", port, seconds);

        final GpioController gpio = GpioFactory.getInstance();
        GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(port);
        pin.high();
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            logger.error("Interrupted while waiting", e);
        }
        pin.low();

        gpio.shutdown();
    }
}