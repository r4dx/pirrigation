package com.pirrigation.scheduler;

import com.pirrigation.event.Event;

import java.io.Closeable;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by r4dx on 01.05.2016.
 */
public class EventsScheduler implements Closeable {
    private final Supplier<Event> eventSupplier;
    private final ScheduledExecutorService service;
    private final long delay;
    private final TimeUnit timeUnit;
    private Consumer<Event> onEvent;
    private BiConsumer<ZonedDateTime, Long> onEventReschedule;
    private BiConsumer<Throwable, EventsScheduler> onException;

    private ScheduledFuture<?> fetchEventsFuture;
    private ScheduledFuture<?> eventTriggerFuture;

    private ZonedDateTime nextScheduledTime;

    public EventsScheduler(Supplier<Event> eventSupplier, ScheduledExecutorService service, long delay,
                           TimeUnit timeUnit, Consumer<Event> onEvent, BiConsumer<ZonedDateTime, Long> onEventReschedule,
                           BiConsumer<Throwable, EventsScheduler> onException) {

        this.eventSupplier = eventSupplier;
        this.service = service;
        this.delay = delay;
        this.timeUnit = timeUnit;
        this.onEvent = onEvent;
        this.onEventReschedule = onEventReschedule;
        this.onException = onException;
    }

    public void schedule() {
        fetchEventsFuture = service.scheduleWithFixedDelay(() -> rescheduleNextTrigger(), 0, delay, timeUnit);
    }

    private void rescheduleNextTrigger() {
        try {
            Event event = eventSupplier.get();
            ZonedDateTime nextTime = event.getNextTime();
            if (nextTime.equals(nextScheduledTime))
                return;

            nextScheduledTime = nextTime;
            long delaySeconds = getDelaySeconds(nextTime);
            onEventReschedule.accept(nextTime, delaySeconds);

            if (delaySeconds <= 0)
                throw new IllegalArgumentException("Next event occurs in past");

            if (eventTriggerFuture != null && !eventTriggerFuture.cancel(false))
                throw new InterruptedException("Can't reschedule");

            eventTriggerFuture = service.scheduleWithFixedDelay(() -> onEvent.accept(event), delaySeconds, delaySeconds,
                    TimeUnit.SECONDS);
        }
        catch (Throwable e) {
            onException.accept(e, this);
        }
    }

    private long getDelaySeconds(ZonedDateTime dateTime) {
        return ChronoUnit.SECONDS.between(ZonedDateTime.now(), dateTime);
    }

    @Override
    public void close() throws IOException {
        if (!fetchEventsFuture.cancel(true) || !eventTriggerFuture.cancel(true))
            throw new IOException("Cannot cancel scheduled tasks");
    }
}
