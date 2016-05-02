package com.pirrigation.event;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Events;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class GoogleEvent implements Event {
    private final Calendar calendarService;
    private final String calendarId;
    private final String eventId;

    public GoogleEvent(Calendar calendarService,
                       String calendarId, String eventId) {
        this.calendarService = calendarService;
        this.calendarId = calendarId;
        this.eventId = eventId;
    }


    @Override
    public ZonedDateTime getNextTime() {
        try {

            Events events = calendarService.events().instances(calendarId, eventId)
                    .setTimeMin(toGoogleDateTime(ZonedDateTime.now()))
                    .setMaxResults(1).execute();

            if (events.getItems().size() != 1)
                throw new IllegalArgumentException("No event instances found");

            return fromGoogleDateTime(events.getItems().get(0).getStart().getDateTime());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ZonedDateTime fromGoogleDateTime(DateTime dateTime) {
        // not the fastest way but least hackiest I've figured
        return ZonedDateTime.parse(dateTime.toStringRfc3339(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    private DateTime toGoogleDateTime(ZonedDateTime dateTime) {
        return new DateTime(Date.from(dateTime.toInstant()));
    }

    @Override
    public String toString() {
        return String.format("Calendar: '%s', eventId: '%s'", calendarId, eventId);
    }
}
