package com.pirrigation.factory;

import com.pirrigation.config.PumpConfig;
import com.pirrigation.config.SchedulerConfig;
import com.pirrigation.event.Event;
import com.pirrigation.water.Pump;
import org.junit.Test;

import java.time.Duration;
import java.util.function.Supplier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by r4dx on 11.06.2016.
 */
public class PirrigationServiceFactoryTest {
    @Test
    public void pirrigationServiceDoesNotThrowException() {
        PirrigationServiceFactory factory = new PirrigationServiceFactory();
        SchedulerConfig config = mock(SchedulerConfig.class);
        when(config.getCheckFrequency()).thenReturn(Duration.ofMillis(1));
        factory.pirrigationService(config, mock(PumpConfig.class), mock(Pump.class), mock(Supplier.class));
    }
}
