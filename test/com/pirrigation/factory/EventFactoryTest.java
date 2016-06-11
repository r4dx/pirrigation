package com.pirrigation.factory;

import com.google.api.services.calendar.Calendar;
import com.pirrigation.config.GoogleCalendarConfig;
import com.pirrigation.event.Event;
import com.typesafe.config.Config;
import org.junit.Test;

import java.util.function.Supplier;

import static org.mockito.Mockito.mock;

/**
 * Created by r4dx on 11.06.2016.
 */
public class EventFactoryTest {
    private final EventFactory factory = new EventFactory();

    @Test
    public void testEventSupplierDoesNotThrowException() {
        factory.eventSupplier(mock(GoogleCalendarConfig.class), mock(Calendar.class));
    }

    @Test
    public void testGoogleCalendarConfigDoesNotThrowException() {
        factory.googleCalendarConfig(mock(Config.class));
    }

    @Test
    public void testGetEventDoesNotThrowException() {
        Supplier<Event> supplier = factory.eventSupplier(mock(GoogleCalendarConfig.class),
                mock(Calendar.class));
        supplier.get();
    }
}
