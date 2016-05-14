package com.pirrigation.scheduler;

import com.pirrigation.event.Event;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;


/**
 * Created by r4dx on 03.05.2016.
 */
public class EventsSchedulerTest {
    private final int REPEAT_TIMES = 10;
    private final ScheduledExecutorServiceMockProvider executorsProvider = new ScheduledExecutorServiceMockProvider();
    private final EventMockProvider eventMockProvider = new EventMockProvider();

    @Test
    public void testSchedule() {
        Event event = eventMockProvider.mockEvent();
        AtomicInteger eventsCounter = new AtomicInteger();

        EventsScheduler eventsScheduler = getScheduler(
                currentEvent ->  {
                    Assert.assertEquals(currentEvent, event);
                    eventsCounter.incrementAndGet();
                },
                (time, seconds) ->  Assert.assertEquals(time.toInstant().toEpochMilli(),
                        event.getNextTime().toInstant().toEpochMilli()));

        eventsScheduler.schedule(event);
        Assert.assertEquals(REPEAT_TIMES, eventsCounter.get());
    }

    private EventsScheduler getScheduler(Consumer<Event> onEvent, BiConsumer<ZonedDateTime, Long> onReschedule,
                                         Supplier<ScheduledFuture> futureSupplier, Integer repeatTimes) {
        return new EventsScheduler(
                executorsProvider.callCallbacksImmediatelyInsteadOfFixedDelay(
                        repeatTimes == null ? REPEAT_TIMES : repeatTimes, futureSupplier),
                onEvent,
                onReschedule);
    }

    private EventsScheduler getScheduler(Consumer<Event> onEvent, BiConsumer<ZonedDateTime, Long> onReschedule,
                                         Supplier<ScheduledFuture> futureSupplier) {
        return getScheduler(onEvent, onReschedule, futureSupplier, null);
    }

    private EventsScheduler getScheduler(Consumer<Event> onEvent, BiConsumer<ZonedDateTime, Long> onReschedule,
                                         Integer repeatTimes) {
        return getScheduler(onEvent, onReschedule, null, repeatTimes);
    }

    private EventsScheduler getScheduler(Consumer<Event> onEvent, BiConsumer<ZonedDateTime, Long> onReschedule) {
        return getScheduler(onEvent, onReschedule, null, null);
    }

    @Test
    public void testReschedule() {
        AtomicInteger rescheduleCounter = new AtomicInteger();
        Event event1 = eventMockProvider.mockEvent();
        Event event2 = eventMockProvider.mockEvent();
        List<Event> events = new ArrayList<>(Arrays.asList(event1, event2));

        EventsScheduler eventsScheduler = getScheduler(
                event -> {
                    if (!events.contains(event))
                        Assert.fail();

                    events.remove(event);
                },
                (time, seconds) ->  rescheduleCounter.incrementAndGet(),
                1);

        eventsScheduler.schedule(event1);
        eventsScheduler.schedule(event2);
        Assert.assertEquals(2, rescheduleCounter.get());
        Assert.assertEquals(0, events.size());
    }

    @Test
    public void testNoReschedulesIfEventIsTheSame() {
        AtomicInteger rescheduleCounter = new AtomicInteger();
        Event event = eventMockProvider.mockEvent();

        EventsScheduler eventsScheduler = getScheduler(
                currentEvent ->  {},
                (time, seconds) ->  rescheduleCounter.incrementAndGet());

        eventsScheduler.schedule(event);
        eventsScheduler.schedule(event);
        Assert.assertEquals(1, rescheduleCounter.get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEventOccursInThePast() {
        Event event = eventMockProvider.mockEventInPast();
        EventsScheduler eventsScheduler = getScheduler(event1 -> {}, (zonedDateTime, aLong) -> {});
        eventsScheduler.schedule(event);
    }

    @Test(expected = IllegalStateException.class)
    public void testEventCantBeCancelledThusCantBeRescheduled() {
        EventsScheduler eventsScheduler = getScheduler(event -> {}, (zonedDateTime, aLong) -> {},
                executorsProvider::mockFutureThatCannotBeCancelled);

        eventsScheduler.schedule(eventMockProvider.mockEvent());
        eventsScheduler.schedule(eventMockProvider.mockEvent());
    }

    @Test
    public void testCanClose() throws IOException {
        EventsScheduler eventsScheduler = getScheduler(event -> {}, (zonedDateTime, aLong) -> {});

        eventsScheduler.schedule(eventMockProvider.mockEvent());
        eventsScheduler.close();
    }

    @Test
    public void testCanCloseBecauseNotOpened() throws IOException {
        EventsScheduler eventsScheduler = getScheduler(event -> {}, (zonedDateTime, aLong) -> {});
        eventsScheduler.close();
    }

    @Test(expected = IOException.class)
    public void testCantClose() throws IOException {
        EventsScheduler eventsScheduler = getScheduler(event -> {}, (zonedDateTime, aLong) -> {},
                executorsProvider::mockFutureThatCannotBeCancelled);

        eventsScheduler.schedule(eventMockProvider.mockEvent());
        eventsScheduler.close();
    }
}
