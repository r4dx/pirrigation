package com.pirrigation.factory;

import com.google.api.services.calendar.Calendar;
import com.pirrigation.config.GoogleCalendarConfig;
import com.pirrigation.config.GoogleCalendarConfigImpl;
import com.pirrigation.event.Event;
import com.pirrigation.event.GoogleEvent;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.function.Supplier;

/**
 * Created by r4dx on 11.06.2016.
 */
@Configuration
public class EventFactory {
    private static final Logger logger = LoggerFactory.getLogger(EventFactory.class);

    private Event getEvent(GoogleCalendarConfig config, Calendar calendar) {
        return new GoogleEvent(calendar, config.getCalendarId(), config.getEventId());
    }

    @Bean
    public Supplier<Event> eventSupplier(GoogleCalendarConfig config, Calendar calendar) {
        return () -> getEvent(config, calendar);
    }

    @Bean
    @Scope("singleton")
    public GoogleCalendarConfig googleCalendarConfig(Config config) {
        GoogleCalendarConfig result = new GoogleCalendarConfigImpl(config);
        logger.info("Loaded GoogleCalendarConfig: " + result.toString());
        return result;
    }
}
