package com.pirrigation;

import com.google.api.services.calendar.Calendar;
import com.pi4j.io.gpio.*;
import com.pirrigation.calendar.GoogleCalendarService;
import com.pirrigation.calendar.GoogleEvent;
import com.pirrigation.calendar.iCal4JRecurrence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.GeneralSecurityException;


public class PirrigationApp {
    private static final Logger logger = LoggerFactory.getLogger(PirrigationApp.class);

    public static void main(String[] args) {

        calendarTest();
    }

    private static void calendarTest() {
        GoogleEvent event = new GoogleEvent(getCalendarService(),
                recurrenceString -> new iCal4JRecurrence(recurrenceString),
                "magicforesterrors@gmail.com", "64o3ep9gcgpm2b9l75j36b9k6cp3cbb1c5ijeb9p6op36oj6cpi32p1k60");
        logger.info(event.getNextTime().toString());
    }

    private static Calendar getCalendarService() {
        try (InputStream is = new FileInputStream("conf/google_client_secret.json")) {
            return new GoogleCalendarService(is, "Pirrigation").get();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }



    private static void gpioTest() {
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