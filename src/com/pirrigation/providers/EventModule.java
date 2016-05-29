package com.pirrigation.providers;

import com.google.api.services.calendar.Calendar;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.pirrigation.config.GoogleCalendarConfig;
import com.pirrigation.config.GoogleCalendarConfigImpl;
import com.pirrigation.event.Event;
import com.pirrigation.event.GoogleEvent;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

/**
 * Created by r4dx on 09.05.2016.
 */
public class EventModule extends AbstractModule {
    private static final Logger logger = LoggerFactory.getLogger(EventModule.class);

    private Event getEvent(GoogleCalendarConfig config, Calendar calendar) {
        return new GoogleEvent(calendar, config.getCalendarId(), config.getEventId());
    }

    @Provides
    public Supplier<Event> provideEventSupplier(GoogleCalendarConfig config, Calendar calendar) {
        return () -> getEvent(config, calendar);
    }

    @Provides
    @Singleton
    public GoogleCalendarConfig provideGoogleCalendarConfig(Config config) {
        GoogleCalendarConfig result = new GoogleCalendarConfigImpl(config);
        logger.info("Loaded GoogleCalendarConfig: " + result.toString());
        return result;
    }

    @Override
    protected void configure() {

    }
}
