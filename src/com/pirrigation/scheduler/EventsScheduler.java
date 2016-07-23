package com.pirrigation.scheduler;

import com.pirrigation.event.Event;

import java.io.Closeable;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by r4dx on 01.05.2016.
 */
public class EventsScheduler implements Closeable {
    private final ScheduledExecutorService service;
    private final Consumer<Event> onEvent;
    private final BiConsumer<ZonedDateTime, Long> onEventReschedule;

    private ScheduledFuture<?> eventTriggerFuture;

    private ZonedDateTime nextScheduledTime;

    public EventsScheduler(ScheduledExecutorService service,
                           Consumer<Event> onEvent, BiConsumer<ZonedDateTime, Long> onEventReschedule) {

        this.service = service;
        this.onEvent = onEvent;
        this.onEventReschedule = onEventReschedule;
    }

    public void schedule(Event event) {
        ZonedDateTime nextTime = event.getNextTime();
        if (nextTime.equals(nextScheduledTime))
            return;

        long delayNanos = getDelayNanos(nextTime);

        if (delayNanos <= 0)
            throw new IllegalArgumentException("Next event occurs in past");

        if (eventTriggerFuture != null && isFutureRunning(eventTriggerFuture))
            throw new IllegalStateException("Can't reschedule");

        if (eventTriggerFuture != null)
            eventTriggerFuture.cancel(true);

        eventTriggerFuture = service.schedule(() -> onEvent.accept(event),
                delayNanos, TimeUnit.NANOSECONDS);

        nextScheduledTime = nextTime;
        onEventReschedule.accept(nextTime, delayNanos);
    }

    private boolean isFutureRunning(ScheduledFuture<?> future) {
        return future.getDelay(TimeUnit.NANOSECONDS) <= 0;
    }

    private long getDelayNanos(ZonedDateTime dateTime) {
        return ChronoUnit.NANOS.between(ZonedDateTime.now(), dateTime);
    }

    @Override
    public void close() throws IOException {
        if (eventTriggerFuture != null)
            if (!eventTriggerFuture.cancel(true))
                throw new IOException("Cannot cancel scheduled tasks");
    }
}
