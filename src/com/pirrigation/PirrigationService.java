package com.pirrigation;

import com.pirrigation.config.PumpConfig;
import com.pirrigation.config.SchedulerConfig;
import com.pirrigation.event.Event;
import com.pirrigation.scheduler.EventsScheduler;
import com.pirrigation.scheduler.FutureUtils;
import com.pirrigation.scheduler.GoogleEventsScheduledFetcher;

import com.pirrigation.water.Pump;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.time.ZonedDateTime;
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
    private final SchedulerConfig schedulerConfig;
    private final PumpConfig pumpConfig;
    private Supplier<Event> eventSupplier;

    private Event nextEvent;
    private ZonedDateTime lastOccurrenceTime;

    public PirrigationService(SchedulerConfig schedulerConfig, PumpConfig pumpConfig,
                              Pump pump, Supplier<Event> eventSupplier) {
        this.schedulerConfig = schedulerConfig;
        this.pumpConfig = pumpConfig;
        this.eventSupplier = eventSupplier;
        this.scheduledService = Executors.newScheduledThreadPool(schedulerConfig.getPoolSize());
        this.pump = pump;
        eventsScheduler = constructScheduler();
        fetcher = constructFetcher();
    }

    private GoogleEventsScheduledFetcher constructFetcher() {
        return new GoogleEventsScheduledFetcher(eventSupplier,
                scheduledService, schedulerConfig.getCheckFrequency().getSeconds(), TimeUnit.SECONDS,
                event -> {
                    this.nextEvent = event;
                    logger.debug("onFetchEvent: {}", event);
                    eventsScheduler.schedule(event);
                },
                this::onErrorConsumer);
    }

    private EventsScheduler constructScheduler() {
        return new EventsScheduler(scheduledService,
                event -> {
                    try {
                        logger.info("onEvent: {}", event);
                        lastOccurrenceTime = ZonedDateTime.now();
                        pump.start(pumpConfig.getWorkDuration());
                    }
                    catch (Throwable e) {
                        logger.error("Problems while pumping water", e);
                    }
                },
                (newTime, nanos) -> logger.info("Rescheduled, next nextEvent will be triggered at {} ({}s from now)",
                        newTime, TimeUnit.SECONDS.convert(nanos, TimeUnit.NANOSECONDS)), new FutureUtils());
    }

    public void serve() {
        logger.info("Starting service");
        fetcher.schedule();
    }

    public Event getNextEvent() {
        return nextEvent;
    }

    public ZonedDateTime getLastOccurrenceTime() {
        return lastOccurrenceTime;
    }

    private void onErrorConsumer(Throwable e) {
        logger.error("Error while scheduling nextEvent", e);
    }

    @Override
    public void close() throws IOException {
        eventsScheduler.close();
        fetcher.close();
        scheduledService.shutdown();
    }
}
