package com.pirrigation.http.controller;

import com.pirrigation.PirrigationService;
import com.pirrigation.event.Event;
import com.pirrigation.event.GoogleEvent;
import com.pirrigation.http.controller.responses.PumpPostResponse;
import com.pirrigation.http.controller.responses.StatusGetResponse;
import com.pirrigation.water.Pump;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.ZonedDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by r4dx on 11.06.2016.
 */
public class HttpStatusControllerTest {

    private ZonedDateTime lastOccurrence;
    private ZonedDateTime nextOccurrence;
    private HttpStatusController controller;

    @Before
    public void setup() {
        lastOccurrence = ZonedDateTime.now().minusDays(1);
        nextOccurrence = ZonedDateTime.now().plusDays(1);
        doSetup();
    }

    public void doSetup() {
        PirrigationService service = mock(PirrigationService.class);
        Event event = mock(Event.class);
        when(event.getNextTime()).thenReturn(nextOccurrence);
        when(service.getLastOccurrenceTime()).thenReturn(lastOccurrence);
        when(service.getNextEvent()).thenReturn(event);
        controller = new HttpStatusController(service);
    }

    private void setupWithoutLastEvent() {
        lastOccurrence = null;
        nextOccurrence = ZonedDateTime.now().plusDays(1);
        doSetup();
    }

    private void setupWithoutNextEvent() {
        lastOccurrence = ZonedDateTime.now().minusDays(1);
        nextOccurrence = null;
        doSetup();
    }

    @Test
    public void testStatus() {
        StatusGetResponse response = controller.getStatus();
        Assert.assertEquals(response.getNextEvent(), nextOccurrence);
        Assert.assertEquals(response.getLastTimeEventHappened(), lastOccurrence);
        Assert.assertTrue(controller.getStatus().getSecondsToNextEvent().isPresent());
        Assert.assertTrue(controller.getStatus().getSecondsFromLastEvent().isPresent());
    }

    @Test
    public void testEmptyIfLastEventNeverOccurred() {
        setupWithoutLastEvent();
        Assert.assertFalse(controller.getStatus().getSecondsFromLastEvent().isPresent());
    }

    @Test
    public void testEmptyIfNextEventNeverScheduled() {
        setupWithoutNextEvent();
        Assert.assertFalse(controller.getStatus().getSecondsToNextEvent().isPresent());
    }
}
