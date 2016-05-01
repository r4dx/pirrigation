package com.pirrigation.calendar;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.common.base.Function;
import java.io.IOException;
import java.time.*;

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
    public LocalDateTime getNextTime() {
        try {
            com.google.api.services.calendar.model.Event event =
                    calendarService.events().get(calendarId, eventId).execute();
            if (event.getRecurrence().size() != 1)
                throw new RuntimeException("More then one / zero recurrences are found!");

            Recurrence recurrence = recurrenceParserFactory.apply(standartify(event.getRecurrence().get(0)));
            return recurrence.getNextDate(googleDateToLocalDate(event.getStart().getDateTime()))
                    .atTime(googleTimeToLocalTime(event.getStart().getDateTime()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String standartify(String recurrenceStr) {
        return recurrenceStr.replace("RRULE:", "");
    }

    private LocalDate googleDateToLocalDate(DateTime date) {
        return googleDateTimeToLocalDateTime(date).toLocalDate();
    }

    private LocalTime googleTimeToLocalTime(DateTime time) {
        return googleDateTimeToLocalDateTime(time).toLocalTime();
    }

    private LocalDateTime googleDateTimeToLocalDateTime(DateTime dateTime) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(dateTime.getValue()), ZoneId.of("UTC"));
    }
}
