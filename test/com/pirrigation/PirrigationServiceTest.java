package com.pirrigation;

import com.pirrigation.config.PirrigationServiceConfig;
import com.pirrigation.event.Event;
import com.pirrigation.water.Pump;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Supplier;

import static org.mockito.Mockito.*;

/**
 * Created by r4dx on 09.05.2016.
 */
public class PirrigationServiceTest {

    private PirrigationService service;

    @Before
    public void setup() {
        service = new PirrigationService(mock(PirrigationServiceConfig.class), mock(Pump.class),
                mock(Supplier.class));
    }

    @Test
    public void testServe() {

    }
}
