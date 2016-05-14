package com.pirrigation.event;

import com.google.api.client.util.DateTime;
import org.junit.Assert;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Created by r4dx on 06.05.2016.
 */
public class GoogleTimeTest {

    @Test
    public void testGoogleToZoned() {
        DateTime google = new DateTime(new Date());
        ZonedDateTime zoned = new GoogleTime(google).getZoned();
        Assert.assertTrue(checkEquals(google, zoned));
    }

    private boolean checkEquals(DateTime google, ZonedDateTime zoned) {
        return google.getValue() == zoned.toInstant().toEpochMilli();
    }

    @Test
    public void testZonedToGoogle() {

        ZonedDateTime zoned = ZonedDateTime.now();
        DateTime google = new GoogleTime(zoned).get();
        Assert.assertTrue(checkEquals(google, zoned));
    }

}
