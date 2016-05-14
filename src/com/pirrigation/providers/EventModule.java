package com.pirrigation.providers;

import com.google.api.services.calendar.Calendar;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.pirrigation.config.PirrigationServiceConfig;
import com.pirrigation.event.Event;
import com.pirrigation.event.GoogleEvent;

import java.util.function.Supplier;

/**
 * Created by r4dx on 09.05.2016.
 */
public class EventModule extends AbstractModule {
    private Event getEvent(PirrigationServiceConfig config, Calendar calendar) {
        return new GoogleEvent(calendar, config.getCalendarId(), config.getEventId());
    }

    @Provides
    public Supplier<Event> provideEventSupplier(PirrigationServiceConfig config, Calendar calendar) {
        return () -> getEvent(config, calendar);
    }

    @Override
    protected void configure() {

    }
}
