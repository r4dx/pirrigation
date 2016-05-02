package com.pirrigation.event;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.common.base.Function;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class GoogleEvent implements Event {
    private final Calendar calendarService;
    private Function<String, Recurrence> recurrenceParserFactory;
    private final String calendarId;
    private final String eventId;

    public GoogleEvent(Calendar calendarService, Function<String, Recurrence> recurrenceFactory,
                       String calendarId, String eventId) {
        this.calendarService = calendarService;
        this.recurrenceParserFactory = recurrenceFactory;
        this.calendarId = calendarId;
        this.eventId = eventId;
    }


    @Override
    public ZonedDateTime getNextTime() {
        try {
            com.google.api.services.calendar.model.Event event =
                    calendarService.events().get(calendarId, eventId).execute();
            if (event.getRecurrence().size() != 1)
                throw new RuntimeException("More then one / zero recurrences are found!");

            Recurrence recurrence = recurrenceParserFactory.apply(standartify(event.getRecurrence().get(0)));
            return recurrence.getNextDate(fromGoogleDateTime(event.getStart().getDateTime()))
                    .atTime(fromGoogleTime(event.getStart().getDateTime()).toOffsetDateTime().toOffsetTime())
                    .toZonedDateTime();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String standartify(String recurrenceStr) {
        return recurrenceStr.replace("RRULE:", "");
    }

    private ZonedDateTime fromGoogleTime(DateTime time) {
        return fromGoogleDateTime(time);

    }

    private ZonedDateTime fromGoogleDateTime(DateTime dateTime) {
        // not the fastest way but least hackiest I've figured
        return ZonedDateTime.parse(dateTime.toStringRfc3339(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    @Override
    public String toString() {
        return String.format("Calendar: '%s', eventId: '%s'", calendarId, eventId);
    }
}
