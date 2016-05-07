package com.pirrigation.scheduler;

import com.pirrigation.event.Event;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

/**
 * Created by r4dx on 03.05.2016.
 */
public class GoogleEventsScheduledFetcher {
    private final int REPEAT_TIMES = 10;
    private final int EVENT_DAYS_RANDOM_BOUND = 256;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testSchedule() {
        // new event each time
        ScheduleResultSummary summary = new ScheduleResultSummary(REPEAT_TIMES, () -> mockEvent());
        summary.schedule();
        Assert.assertEquals(REPEAT_TIMES, summary.getRescheduleCount());
    }

    @Test
    public void testNoReschedulesIfEventIsTheSame() {
        // one event every time
        Event event = mockEvent();
        ScheduleResultSummary summary = new ScheduleResultSummary(REPEAT_TIMES, () -> event);
        summary.schedule();
        Assert.assertEquals(1, summary.getRescheduleCount());
    }

    @Test
    public void testEventOccursInThePast() {
        expectedException.expectCause(is(IsInstanceOf.instanceOf(IllegalArgumentException.class)));

        Event event = mockEventInPast();
        ScheduleResultSummary summary = new ScheduleResultSummary(1, () -> event);
        summary.schedule();
    }

    @Test
    public void testEventCantBeCancelledThusCantBeRescheduled() {
        expectedException.expectCause(is(IsInstanceOf.instanceOf(InterruptedException.class)));
        ScheduleResultSummary summary = new ScheduleResultSummary(REPEAT_TIMES, () -> mockEvent(),
                null, () -> mockUncancellableFuture());
        summary.schedule();
    }

    @Test
    public void testExceptionDelegateWorks() {
        ScheduleResultSummary summary = new ScheduleResultSummary(REPEAT_TIMES, () -> mockEvent(),
                null, () -> mockUncancellableFuture(), true);
        summary.schedule();
        Assert.assertEquals(REPEAT_TIMES - 1, summary.getExceptionsCount());
    }

    private ScheduledFuture mockUncancellableFuture() {
        ScheduledFuture result = mock(ScheduledFuture.class);
        when(result.cancel(anyBoolean())).thenReturn(false);
        return result;
    }

    @Test
    public void testEventsOccur() {
        // one event every time
        Event event = mockEvent();
        ScheduleResultSummary summary = new ScheduleResultSummary(REPEAT_TIMES, () -> event);
        summary.schedule();
        Assert.assertEquals(REPEAT_TIMES, summary.getEventCount());
    }

    @Test
    public void testCanClose() throws IOException {
        ScheduleResultSummary summary = new ScheduleResultSummary(REPEAT_TIMES, () -> mockEvent());
        summary.schedule();
        summary.close();
    }

    @Test
    public void testCanCloseBecauseWasntOpen() throws IOException {
        ScheduleResultSummary summary = new ScheduleResultSummary(REPEAT_TIMES, () -> mockEvent());
        summary.close();
    }

    @Test(expected = IOException.class)
    public void testCantClose() throws IOException {
        ScheduleResultSummary summary = new ScheduleResultSummary(REPEAT_TIMES, () -> mockEvent(),
                () -> mockUncancellableFuture(), () -> mockUncancellableFuture(), true);
        summary.schedule();
        summary.close();
    }

    @Test(expected = IOException.class)
    public void testCantCloseBecauseCantInterruptEventTrigger() throws IOException {
        ScheduleResultSummary summary = new ScheduleResultSummary(REPEAT_TIMES, () -> mockEvent(),
                null, () -> mockUncancellableFuture(), true);
        summary.schedule();
        summary.close();
    }

    @Test(expected = IOException.class)
    public void testCantCloseBecauseCantInterruptFetchFuture() throws IOException {
        ScheduleResultSummary summary = new ScheduleResultSummary(REPEAT_TIMES, () -> mockEvent(),
                () -> mockUncancellableFuture(), null, true);
        summary.schedule();
        summary.close();
    }

    private Event mockEvent() {
        Event mockedEvent = mock(Event.class);
        when(mockedEvent.getNextTime()).thenReturn(ZonedDateTime.now().plusDays(new Random().nextInt(
                EVENT_DAYS_RANDOM_BOUND) + 1));
        return mockedEvent;
    }

    private Event mockEventInPast() {
        Event mockedEvent = mock(Event.class);
        when(mockedEvent.getNextTime()).thenReturn(ZonedDateTime.now().minusDays(new Random().nextInt(
                EVENT_DAYS_RANDOM_BOUND) + 1));
        return mockedEvent;
    }
}
