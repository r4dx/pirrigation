package com.pirrigation.scheduler;

import com.pirrigation.event.Event;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by r4dx on 03.05.2016.
 */
public class ScheduleResultSummary {
    private AtomicInteger rescheduleCounter = new AtomicInteger();
    private AtomicInteger eventCounter = new AtomicInteger();
    private AtomicInteger exceptionsCounter = new AtomicInteger();
    private Supplier<Event> supplier;
    private Supplier<ScheduledFuture> futureSupplier;
    private Supplier<ScheduledFuture> eventsFutureSupplier;
    private boolean countExceptionsAndNotThrowThem;

    private EventsScheduler scheduler;

    public ScheduleResultSummary(int schedulerRepeatTimes, Supplier<Event> eventSupplier,
                                 Supplier<ScheduledFuture> futureSupplier,
                                 Supplier<ScheduledFuture> eventsFutureSupplier,
                                 boolean countExceptionsAndNotThrowThem) {
        this.supplier = eventSupplier;
        this.futureSupplier = futureSupplier;
        this.eventsFutureSupplier = eventsFutureSupplier;
        this.countExceptionsAndNotThrowThem = countExceptionsAndNotThrowThem;

        ScheduledExecutorService service = callCallbacksImmediatelyInsteadOfFixedDelay(schedulerRepeatTimes,
                futureSupplier);

        ScheduledExecutorService eventsService = callCallbacksImmediatelyInsteadOfFixedDelay(schedulerRepeatTimes,
                eventsFutureSupplier);

        scheduler = new EventsScheduler(supplier, service, eventsService, 10, TimeUnit.MICROSECONDS,
                (event) -> eventCounter.incrementAndGet(),
                (zonedDateTime, secondsToGo) -> rescheduleCounter.incrementAndGet(),
                (e, eventsScheduler) ->
                {
                    if (!countExceptionsAndNotThrowThem)
                        throw new RuntimeException(e);

                    exceptionsCounter.incrementAndGet();
                });
    }

    public ScheduleResultSummary(int schedulerRepeatTimes, Supplier<Event> eventSupplier,
                                 Supplier<ScheduledFuture> futureSupplier, Supplier<ScheduledFuture> eventsFutureSupplier) {
        this(schedulerRepeatTimes, eventSupplier, futureSupplier, eventsFutureSupplier, false);
    }

    public ScheduleResultSummary(int schedulerRepeatTimes, Supplier<Event> eventSupplier) {
        this(schedulerRepeatTimes, eventSupplier, null, null, false);
    }

    public void schedule() {
        scheduler.schedule();
    }

    public void close() throws IOException {
        scheduler.close();
    }

    private ScheduledExecutorService callCallbacksImmediatelyInsteadOfFixedDelay(
            int repeatTimes, Supplier<ScheduledFuture> futureSupplier) {
        ScheduledExecutorService service = mock(ScheduledExecutorService.class);
        when(service.scheduleWithFixedDelay(notNull(Runnable.class), anyLong(), anyLong(), any(TimeUnit.class)))
                .thenAnswer((invocation -> {
                    Runnable callback = ((Runnable)(invocation.getArguments()[0]));
                    for (int i = 0; i < repeatTimes; i++)
                        callback.run();

                    return futureSupplier == null ? mockFuture() : futureSupplier.get();
                }));
        return service;
    }

    private ScheduledFuture mockFuture() {
        ScheduledFuture result = mock(ScheduledFuture.class);
        when(result.cancel(anyBoolean())).thenReturn(true);
        return result;
    }

    public int getRescheduleCount() {
        return rescheduleCounter.get();
    }

    public int getEventCount() {
        return eventCounter.get();
    }

    public int getExceptionsCount() {
        return exceptionsCounter.get();
    }
}
