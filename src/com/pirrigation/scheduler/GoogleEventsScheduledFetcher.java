package com.pirrigation.scheduler;

import com.pirrigation.event.Event;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by r4dx on 01.05.2016.
 */
public class GoogleEventsScheduledFetcher implements Closeable {
    private final Supplier<Event> eventSupplier;
    private final ScheduledExecutorService service;
    private final long delay;
    private final TimeUnit timeUnit;
    private Consumer<Event> onFetchEvent;

    private ScheduledFuture<?> fetchEventsFuture;

    public GoogleEventsScheduledFetcher(Supplier<Event> eventSupplier,
                                        ScheduledExecutorService service,
                                        long delay,
                                        TimeUnit timeUnit,
                                        Consumer<Event> onFetchEvent) {

        this.eventSupplier = eventSupplier;
        this.service = service;
        this.delay = delay;
        this.timeUnit = timeUnit;
        this.onFetchEvent = onFetchEvent;
    }

    public void schedule() {
        if (fetchEventsFuture != null)
            throw new IllegalArgumentException("Already scheduled");

        fetchEventsFuture = service.scheduleWithFixedDelay(() -> rescheduleNextTrigger(), 0, delay, timeUnit);
    }

    private void rescheduleNextTrigger() {
        Event event = eventSupplier.get();
        onFetchEvent.accept(event);
    }

    @Override
    public void close() throws IOException {
        if (fetchEventsFuture != null)
            if (!fetchEventsFuture.cancel(true))
                throw new IOException("Cannot cancel scheduled tasks");
    }
}
