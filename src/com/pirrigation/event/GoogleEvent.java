package com.pirrigation.event;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Events;
import java.io.IOException;
import java.time.*;

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
        Events events;
        try {
            events = calendarService.events().instances(calendarId, eventId)
                    .setTimeMin(new GoogleTime(ZonedDateTime.now()).get())
                    .setMaxResults(1).execute();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (events.getItems().size() != 1)
            throw new IllegalArgumentException("No or too many event instances found");

        return new GoogleTime(events.getItems().get(0).getStart().getDateTime()).getZoned();
    }

    @Override
    public String toString() {
        return String.format("Calendar: '%s', eventId: '%s'", calendarId, eventId);
    }
}
