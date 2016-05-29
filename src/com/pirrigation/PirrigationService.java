package com.pirrigation;

import com.pirrigation.config.PirrigationServiceConfig;
import com.pirrigation.event.Event;
import com.pirrigation.scheduler.EventsScheduler;
import com.pirrigation.scheduler.GoogleEventsScheduledFetcher;

import com.pirrigation.water.Pump;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Created by r4dx on 07.05.2016.
 */
public class PirrigationService implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(PirrigationApp.class);

    private final ScheduledExecutorService scheduledService;
    private final Pump pump;

    private final EventsScheduler eventsScheduler;
    private final GoogleEventsScheduledFetcher fetcher;
    private final PirrigationServiceConfig config;
    private Supplier<Event> eventSupplier;

    public PirrigationService(PirrigationServiceConfig config, Pump pump, Supplier<Event> eventSupplier) {
        this.config = config;
        this.eventSupplier = eventSupplier;
        this.scheduledService = Executors.newScheduledThreadPool(config.getPoolSize());
        this.pump = pump;
        eventsScheduler = constructScheduler();
        fetcher = constructFetcher();
    }

    private GoogleEventsScheduledFetcher constructFetcher() {
        return new GoogleEventsScheduledFetcher(eventSupplier,
                scheduledService, config.getCheckFrequency().getSeconds(), TimeUnit.SECONDS,
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
                        pump.start(config.getPumpWorkDuration());
                    }
                    catch (Throwable e) {
                        logger.error("Problems while pumping water", e);
                    }
                },
                (newTime, nanos) -> logger.info("Rescheduled, next event will be triggered at {} ({}s from now)",
                        newTime, TimeUnit.SECONDS.convert(nanos, TimeUnit.NANOSECONDS)));
    }

    public void serve() {
        logger.info("Starting service");
        logger.info("Configuration:" + config.toString());

        fetcher.schedule();
    }

    private void onErrorConsumer(Throwable e) {
        logger.error("Error while scheduling event", e);
    }

    @Override
    public void close() throws IOException {
        eventsScheduler.close();
        fetcher.close();
        scheduledService.shutdown();
    }
}
