package com.pirrigation;

import com.google.api.services.calendar.Calendar;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.pirrigation.event.Event;
import com.pirrigation.event.GoogleCalendarService;
import com.pirrigation.event.GoogleEvent;
import com.pirrigation.scheduler.EventsScheduler;
import com.pirrigation.scheduler.GoogleEventsScheduledFetcher;
import com.pirrigation.scheduler.Sleeper;
import com.pirrigation.water.PiPump;
import com.pirrigation.water.Pump;
import com.pirrigation.water.StubPump;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by r4dx on 07.05.2016.
 */
class PirrigationService implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(PirrigationApp.class);

    // This block of constants will go to configuration
    private final long PUMP_WORK_MS = 10000;
    private final long CHECK_FREQUENCY_SECONDS = 30;
    private final int POOL_SIZE = 10;
    private final Pin PUMP_CONTROL_PIN = RaspiPin.GPIO_25;
    private final String CALENDAR_ID = "magicforesterrors@gmail.com";
    private final String EVENT_ID = "coqjgpho6cs6ab9i68pjeb9kc9h64b9o68sjab9ockoj2ob469j38p1i68";
    private final String GOOGLE_CLIENT_SECRET_JSON_PATH = "conf/google_client_secret.json";
    private final String GOOGLE_APP_NAME = "Pirrigation";

    private final ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(POOL_SIZE);
    //private final Pump pump = new PiPump(GpioFactory.getInstance(), PUMP_CONTROL_PIN, Sleeper.DEFAULT);
    private Pump pump = new StubPump(Sleeper.DEFAULT);

    private final EventsScheduler eventsScheduler = constructScheduler();
    private final GoogleEventsScheduledFetcher fetcher = constructFetcher();

    private GoogleEventsScheduledFetcher constructFetcher() {
        return new GoogleEventsScheduledFetcher(this::getEvent,
                scheduledService, CHECK_FREQUENCY_SECONDS, TimeUnit.SECONDS,
                event -> {
                    logger.info("onFetchEvent: {}", event);
                    eventsScheduler.schedule(event);
                },
                this::onErrorConsumer);
    }

    private EventsScheduler constructScheduler() {
        return new EventsScheduler(scheduledService,
                event -> {
                    try {
                        logger.info("onEvent: {}", event);
                        pump.start(PUMP_WORK_MS);
                    }
                    catch (Throwable e) {
                        logger.error("Problems while pumping water", e);
                    }
                },
                (newTime, nanos) -> logger.info("Rescheduled, next event will be triggered at {} ({}s from now)", newTime,
                        TimeUnit.SECONDS.convert(nanos, TimeUnit.NANOSECONDS)));
    }

    public void serve() {
        fetcher.schedule();
    }

    private void onErrorConsumer(Throwable e) {
        logger.error("Error while scheduling event", e);
        try {
            close();
        }
        catch (IOException e1){
            logger.error("Error while while closing fetcher or scheduler", e1);
        }
    }

    private Event getEvent() {
        return new GoogleEvent(getCalendarService(), CALENDAR_ID, EVENT_ID);
    }

    private Calendar getCalendarService() {
        try (InputStream is = new FileInputStream(GOOGLE_CLIENT_SECRET_JSON_PATH)) {
            return new GoogleCalendarService(is, GOOGLE_APP_NAME).get();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        eventsScheduler.close();
        fetcher.close();
        scheduledService.shutdown();
    }
}
