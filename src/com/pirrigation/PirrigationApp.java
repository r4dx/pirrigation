package com.pirrigation;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import com.google.api.services.calendar.Calendar;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;
import com.pirrigation.event.Event;
import com.pirrigation.event.GoogleCalendarService;
import com.pirrigation.event.GoogleEvent;
import com.pirrigation.scheduler.EventsScheduler;
import com.pirrigation.water.PiPump;
import com.pirrigation.water.Pump;
import com.pirrigation.water.mocks.MockPump;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class PirrigationApp {
    private static final Logger logger = LoggerFactory.getLogger(PirrigationApp.class);

    private Pump pump = new PiPump(GpioFactory.getInstance(), RaspiPin.GPIO_25);
    //private Pump pump = new MockPump();

    private final ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(10);

    public static void main(String[] args) {

        initLogback();

        new PirrigationApp().serve();
    }

    private static void initLogback() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);

            context.reset();
            configurator.doConfigure("conf/logback.xml");
        } catch (JoranException je) {
        }
        StatusPrinter.printInCaseOfErrorsOrWarnings(context);

    }

    private void serve() {
        final long checkFrequencySeconds = 30;
        final long pumpWorkSeconds = 10;
        EventsScheduler scheduler = new EventsScheduler(() -> getEvent(), scheduledService,
                checkFrequencySeconds, TimeUnit.SECONDS,
                event -> { logger.info("onEvent: {}", event); pump.start(pumpWorkSeconds); },
                (newTime, seconds) -> logger.info("Rescheduled, next event will be triggered at {} ({}s from now)", newTime,
                        seconds),
                (e, eventsScheduler) -> onErrorConsumer(e, eventsScheduler));

        scheduler.schedule();
    }

    private void onErrorConsumer(Throwable e, EventsScheduler scheduler) {
        logger.error("Error while scheduling event", e);
        try {
            scheduler.close();
            scheduledService.shutdown();
        } catch (IOException e1) {
            logger.error("Error while while closing scheduler", e);
        }
    }

    private Event getEvent() {
        final String eventId = "64o3ep9gcgpm2b9l75j36b9k6cp3cbb1c5ijeb9p6op36oj6cpi32p1k60";
        final String calendarId = "magicforesterrors@gmail.com";

        logger.info("Fetching calendar event '{}' from calendar '{}'", eventId, calendarId);
        return new GoogleEvent(getCalendarService(), calendarId, eventId);
    }

    private Calendar getCalendarService() {
        try (InputStream is = new FileInputStream("conf/google_client_secret.json")) {
            return new GoogleCalendarService(is, "Pirrigation").get();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}