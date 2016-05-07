package com.pirrigation.scheduler;

import com.pirrigation.event.Event;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;


/**
 * Created by r4dx on 03.05.2016.
 */
public class GoogleEventsScheduledFetcherTest {
    private final int REPEAT_TIMES = 10;

    private final ScheduledExecutorServiceMockProvider executorsProvider = new ScheduledExecutorServiceMockProvider();
    private final EventMockProvider eventMockProvider = new EventMockProvider();

    @Test
    public void testSchedule() {
        AtomicInteger callCounter = new AtomicInteger();
        Event event = eventMockProvider.mockEvent();
        GoogleEventsScheduledFetcher fetcher = getFetcher(() -> event, currentEvent -> {
            callCounter.incrementAndGet();
            Assert.assertEquals(currentEvent, event);
        });

        fetcher.schedule();
        Assert.assertEquals(REPEAT_TIMES, callCounter.get());
    }

    private GoogleEventsScheduledFetcher getFetcher(Supplier<Event> eventSupplier, Consumer<Event> onEventFetched) {
        return getFetcher(eventSupplier, onEventFetched, null);
    }

    private GoogleEventsScheduledFetcher getFetcher(Supplier<Event> eventSupplier, Consumer<Event> onEventFetched,
                                                    Supplier<ScheduledFuture> futureSupplier) {
        return new GoogleEventsScheduledFetcher(eventSupplier,
                executorsProvider.callCallbacksImmediatelyInsteadOfFixedDelay(REPEAT_TIMES, futureSupplier), 10,
                TimeUnit.SECONDS, onEventFetched);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testScheduleMultipleTimes() {
        GoogleEventsScheduledFetcher fetcher = getFetcher(() -> eventMockProvider.mockEvent(), event -> {});
        fetcher.schedule();
        fetcher.schedule();
    }

    @Test
    public void testCanClose() throws IOException {
        GoogleEventsScheduledFetcher fetcher = getFetcher(() -> eventMockProvider.mockEvent(), event -> {});
        fetcher.schedule();
        fetcher.close();
    }

    @Test
    public void testCanCloseBecauseWasntOpen() throws IOException {
        GoogleEventsScheduledFetcher fetcher = getFetcher(() -> eventMockProvider.mockEvent(), event -> {});
        fetcher.close();
    }

    @Test(expected = IOException.class)
    public void testCantClose() throws IOException {

        GoogleEventsScheduledFetcher fetcher = getFetcher(() -> eventMockProvider.mockEvent(), event -> {},
                () -> executorsProvider.mockUncancellableFuture());
        fetcher.schedule();
        fetcher.close();
    }
}
