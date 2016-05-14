package com.pirrigation.event;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import com.google.api.services.calendar.model.Event;
import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

/**
 * Created by r4dx on 03.05.2016.
 */
public class GoogleEventTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testNext() throws IOException {
        ZonedDateTime zonedCheck = ZonedDateTime.now();
        Calendar calendar = mockCalendar(zonedCheck);
        GoogleEvent googleEvent = new GoogleEvent(calendar, "calendarId", "eventId");
        Assert.assertEquals(zonedCheck.toInstant().toEpochMilli(), googleEvent.getNextTime().toInstant().toEpochMilli());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooManyInstances() throws IOException {
        ZonedDateTime zonedCheck = ZonedDateTime.now();
        Calendar calendar = mockCalendar(Arrays.asList(new Event(), new Event()));
        GoogleEvent googleEvent = new GoogleEvent(calendar, "calendarId", "eventId");
        Assert.assertEquals(zonedCheck.toInstant().toEpochMilli(), googleEvent.getNextTime().toInstant().toEpochMilli());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoInstances() throws IOException {
        ZonedDateTime zonedCheck = ZonedDateTime.now();
        Calendar calendar = mockCalendar(new ArrayList<>());
        GoogleEvent googleEvent = new GoogleEvent(calendar, "calendarId", "eventId");
        Assert.assertEquals(zonedCheck.toInstant().toEpochMilli(), googleEvent.getNextTime().toInstant().toEpochMilli());
    }

    @Test
    public void testIOException() throws IOException {
        expectedException.expectCause(is(IsInstanceOf.instanceOf(IOException.class)));

        Calendar calendar = mockCalendar(new ArrayList<>());
        when(calendar.events().instances(anyString(), anyString())).thenThrow(new IOException());
        GoogleEvent googleEvent = new GoogleEvent(calendar, "calendarId", "eventId");
        googleEvent.getNextTime();
    }

    @Test
    public void testToString() throws IOException {
        Calendar calendar = mockCalendar(ZonedDateTime.now());
        final String calendarId = "calendarId";
        final String eventId = "eventId";

        GoogleEvent event = new GoogleEvent(calendar, calendarId, eventId);
        Assert.assertThat(event.toString(), CoreMatchers.containsString(calendarId));
        Assert.assertThat(event.toString(), CoreMatchers.containsString(eventId));
    }

    private Calendar mockCalendar(ZonedDateTime zonedCheck) throws IOException {
        Event event = new Event();
        EventDateTime dateTime = new EventDateTime();
        event.setStart(dateTime);
        dateTime.setDateTime(new GoogleTime(zonedCheck).get());

        return mockCalendar(Collections.singletonList(event));
    }

    private Calendar mockCalendar(List<Event> eventList) throws IOException {
        Calendar calendar = mock(Calendar.class);
        Calendar.Events events = mock(Calendar.Events.class);
        Calendar.Events.Instances instances = mock(Calendar.Events.Instances.class);
        Events eventInstances = new Events();

        when(calendar.events()).thenReturn(events);
        when(events.instances(anyString(), anyString())).thenReturn(instances);
        when(instances.setTimeMin(any(DateTime.class))).thenReturn(instances);
        when(instances.setMaxResults(anyInt())).thenReturn(instances);
        when(instances.execute()).thenReturn(eventInstances);
        eventInstances.setItems(eventList);

        return calendar;
    }
}
