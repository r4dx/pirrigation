package com.pirrigation.config;

import com.pi4j.io.gpio.RaspiPin;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Created by r4dx on 29.05.2016.
 */
public class PirrigationServiceConfigImplTest {

    private PumpConfig pumpConfig;
    private GoogleCalendarConfig googleCalendarConfig;
    private SchedulerConfig scheduledConfig;

    @Before
    public void setup() {
        final String conf =
                "{\n" +
                        "  \"pump\": {\n" +
                        "    \"workDuration\": \"1s\",\n" +
                        "    \"controlPin\": \"GPIO 25\"\n" +
                        "  },\n" +
                        "\n" +
                        "  \"googleCalendar\": {\n" +
                        "    \"calendarId\": \"calendarId\",\n" +
                        "    \"eventId\": \"eventId\",\n" +
                        "    \"secretJsonPath\": \"secretJsonPath\",\n" +
                        "    \"appName\": \"appName\",\n" +
                        "\n" +
                        "    \"scheduler\": {\n" +
                        "      \"checkFrequency\": \"1s\",\n" +
                        "      \"poolSize\": 1\n" +
                        "    }\n" +
                        "  }\n" +
                        "}";

        Config hoconConfig = ConfigFactory.parseString(conf);
        pumpConfig = new PumpConfigImpl(hoconConfig);
        googleCalendarConfig = new GoogleCalendarConfigImpl(hoconConfig);
        scheduledConfig = new SchedulerConfigImpl(hoconConfig);

    }

    @Test
    public void testPump() {
        Assert.assertEquals(Duration.ofSeconds(1), pumpConfig.getWorkDuration());
        Assert.assertEquals(RaspiPin.GPIO_25, pumpConfig.getControlPin());
    }

    @Test
    public void testGoogleCalendar() {
        Assert.assertEquals("eventId", googleCalendarConfig.getEventId());
        Assert.assertEquals("appName", googleCalendarConfig.getAppName());
        Assert.assertEquals("calendarId", googleCalendarConfig.getCalendarId());
        Assert.assertEquals("secretJsonPath", googleCalendarConfig.getSecretJsonPath());
    }

    @Test
    public void testScheduledConfig() {
        Assert.assertEquals(Duration.ofSeconds(1), scheduledConfig.getCheckFrequency());
        Assert.assertEquals(1, scheduledConfig.getPoolSize());
    }

    @Test
    public void testPumpConfigToString() {
        testToString(PumpConfigImpl.class, pumpConfig);
    }

    @Test
    public void testGoogleCalendarConfigToString() {
        testToString(GoogleCalendarConfigImpl.class, googleCalendarConfig);
    }

    @Test
    public void testSchedulerConfigToString() {
        testToString(SchedulerConfigImpl.class, scheduledConfig);
    }

    private void testToString(Class klass, Object obj) {
        List<Field> fields = getAllFields(klass,
                field -> field.getDeclaringClass().getName().startsWith("com.pirrigation.") &&
                        !field.getName().startsWith("$"));

        for (Field currentField : fields)
            Assert.assertTrue(obj.toString().contains(currentField.getName()));
    }

    private List<Field> getAllFields(Class klass, Predicate<Field> filter) {
        return Arrays.asList(klass.getDeclaredFields()).stream().filter(filter).collect(Collectors.toList());
    }

}
