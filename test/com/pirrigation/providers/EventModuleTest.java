package com.pirrigation.providers;

import com.google.api.services.calendar.Calendar;
import com.pirrigation.config.PirrigationServiceConfig;
import com.pirrigation.event.Event;
import org.junit.Test;

import java.util.function.Supplier;

import static org.mockito.Mockito.mock;

/**
 * Created by r4dx on 09.05.2016.
 */
public class EventModuleTest {
    private EventModule module = new EventModule();

    @Test
    public void testProvideEventSupplierDoesNotThrowException() {
        module.provideEventSupplier(mock(PirrigationServiceConfig.class), mock(Calendar.class));
    }

    @Test
    public void testConfigureDoesNotThrowException() {
        module.configure();
    }

    @Test
    public void testGetEventDoesNotThrowException() {
        Supplier<Event> supplier = module.provideEventSupplier(mock(PirrigationServiceConfig.class),
                mock(Calendar.class));
        supplier.get();
    }
}
