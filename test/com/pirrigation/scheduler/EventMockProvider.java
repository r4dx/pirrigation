package com.pirrigation.scheduler;

import com.pirrigation.event.Event;

import java.time.ZonedDateTime;
import java.util.Random;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by r4dx on 07.05.2016.
 */
class EventMockProvider {
    private final int EVENT_DAYS_RANDOM_BOUND = 256;

    public Event mockEvent() {
        Event mockedEvent = mock(Event.class);
        when(mockedEvent.getNextTime()).thenReturn(ZonedDateTime.now().plusDays(new Random().nextInt(
                EVENT_DAYS_RANDOM_BOUND) + 1));
        return mockedEvent;
    }

    public Event mockEvent(int msFromNow) {
        Event mockedEvent = mock(Event.class);
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime nextTime = now.toInstant().plusMillis(msFromNow).atZone(now.getZone());
        when(mockedEvent.getNextTime()).thenReturn(nextTime);
        return mockedEvent;
    }

    public Event mockEventInPast() {
        Event mockedEvent = mock(Event.class);
        when(mockedEvent.getNextTime()).thenReturn(ZonedDateTime.now().minusDays(new Random().nextInt(
                EVENT_DAYS_RANDOM_BOUND) + 1));
        return mockedEvent;
    }
}
