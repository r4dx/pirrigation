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

import static org.mockito.Mockito.mock;

/**
 * Created by r4dx on 29.05.2016.
 */
public class PirrigationServiceConfigImplTest {

    private PirrigationServiceConfig config;

    @Before
    public void setup() {
        final String conf = "{\n" +
                "  \"pumpWorkDuration\": \"1s\",\n" +
                "  \"checkFrequency\": \"2s\",\n" +
                "  \"poolSize\": 3,\n" +
                "  \"pumpControlPin\": \"GPIO 25\",\n" +
                "  \"calendarId\": \"calendarId\",\n" +
                "  \"eventId\": \"eventId\",\n" +
                "  \"googleClientSecretJsonPath\": \"googleClientSecretJsonPath\",\n" +
                "  \"googleAppName\": \"googleAppName\"\n" +
                "}";

        Config hoconConfig = ConfigFactory.parseString(conf);
        config = new PirrigationServiceConfigImpl(hoconConfig);
    }

    @Test
    public void test() {
        Assert.assertEquals("calendarId", config.getCalendarId());
        Assert.assertEquals("eventId", config.getEventId());
        Assert.assertEquals("googleAppName", config.getGoogleAppName());
        Assert.assertEquals("googleClientSecretJsonPath", config.getGoogleClientSecretJsonPath());
        Assert.assertEquals(RaspiPin.GPIO_25, config.getPumpControlPin());
        Assert.assertEquals(Duration.ofSeconds(1), config.getPumpWorkDuration());
        Assert.assertEquals(Duration.ofSeconds(2), config.getCheckFrequency());
        Assert.assertEquals(3, config.getPoolSize());
    }

    @Test
    public void testToString() {
        for (Field currentField : getAllFields(PirrigationServiceConfigImpl.class,
                field -> field.getName().startsWith("com.pirrigation.") && !field.getName().startsWith("$"))) {
            Assert.assertTrue(config.toString().contains(currentField.getName()));
        }
    }

    private List<Field> getAllFields(Class klass, Predicate<Field> filter) {
        List<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(klass.getDeclaredFields()).stream().filter(filter).collect(Collectors.toList()));
        for (Field field : fields)
            if (filter.test(field))
                fields.addAll(getAllFields(field.getType(), filter));

        return fields;
    }

}
