package com.pirrigation.factory;

import com.typesafe.config.Config;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * Created by r4dx on 11.06.2016.
 */
public class ConfigFactoryTest {
    private ConfigFactory factory = new ConfigFactory();

    @Test
    public void testPumpConfigDoesNotThrowException() {
        factory.pumpConfig(mock(Config.class));
    }

    @Test
    public void testSchedulerConfigDoesNotThrowException() {
        factory.schedulerConfig(mock(Config.class));
    }

    @Test
    public void testHoconConfigDoesNotThrowException() {
        factory.hoconConfig();
    }
}
