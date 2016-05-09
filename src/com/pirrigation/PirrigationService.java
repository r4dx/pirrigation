package com.pirrigation;

import com.google.api.services.calendar.Calendar;
import com.pirrigation.config.PirrigationServiceConfig;
import com.pirrigation.event.Event;
import com.pirrigation.event.GoogleCalendarService;
import com.pirrigation.event.GoogleEvent;
import com.pirrigation.scheduler.EventsScheduler;
import com.pirrigation.scheduler.GoogleEventsScheduledFetcher;

import com.pirrigation.water.Pump;
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

    private final ScheduledExecutorService scheduledService;
    private final Pump pump;

    private final EventsScheduler eventsScheduler;
    private final GoogleEventsScheduledFetcher fetcher;
    private final PirrigationServiceConfig config;

    public PirrigationService(PirrigationServiceConfig config, Pump pump) {

        this.config = config;
        this.scheduledService = Executors.newScheduledThreadPool(config.getPoolSize());
        this.pump = pump;
        eventsScheduler = constructScheduler();
        fetcher = constructFetcher();
    }

    private GoogleEventsScheduledFetcher constructFetcher() {
        return new GoogleEventsScheduledFetcher(this::getEvent,
                scheduledService, config.getCheckFrequencySeconds(), TimeUnit.SECONDS,
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
                        pump.start(config.getPumpWorkMs());
                    }
                    catch (Throwable e) {
                        logger.error("Problems while pumping water", e);
                    }
                },
                (newTime, nanos) -> logger.info("Rescheduled, next event will be triggered at {} ({}s from now)",
                        newTime, TimeUnit.SECONDS.convert(nanos, TimeUnit.NANOSECONDS)));
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
        return new GoogleEvent(getCalendarService(), config.getCalendarId(), config.getEventId());
    }

    private Calendar getCalendarService() {
        try (InputStream is = new FileInputStream(config.getGoogleClientSecretJsonPath())) {
            return new GoogleCalendarService(is, config.getGoogleAppName()).get();
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
