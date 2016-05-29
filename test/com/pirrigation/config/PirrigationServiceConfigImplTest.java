package com.pirrigation.config;

import com.pi4j.io.gpio.RaspiPin;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by r4dx on 29.05.2016.
 */
public class PirrigationServiceConfigImplTest {

    private final String conf = "{\n" +
            "  \"pumpWorkMs\": 1,\n" +
            "  \"checkFrequencySeconds\": 2,\n" +
            "  \"poolSize\": 3,\n" +
            "  \"pumpControlPin\": \"GPIO 25\",\n" +
            "  \"calendarId\": \"calendarId\",\n" +
            "  \"eventId\": \"eventId\",\n" +
            "  \"googleClientSecretJsonPath\": \"googleClientSecretJsonPath\",\n" +
            "  \"googleAppName\": \"googleAppName\"\n" +
            "}";

    @Test
    public void test() {
        Config hoconConfig = ConfigFactory.parseString(conf);
        PirrigationServiceConfig config = new PirrigationServiceConfigImpl(hoconConfig);
        Assert.assertEquals("calendarId", config.getCalendarId());
        Assert.assertEquals("eventId", config.getEventId());
        Assert.assertEquals("googleAppName", config.getGoogleAppName());
        Assert.assertEquals("googleClientSecretJsonPath", config.getGoogleClientSecretJsonPath());
        Assert.assertEquals(RaspiPin.GPIO_25, config.getPumpControlPin());
        Assert.assertEquals(1, config.getPumpWorkMs());
        Assert.assertEquals(2, config.getCheckFrequencySeconds());
        Assert.assertEquals(3, config.getPoolSize());
    }
}
