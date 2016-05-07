package com.pirrigation.event;

import com.google.api.client.util.DateTime;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by r4dx on 06.05.2016.
 */
public class GoogleTime {
    private final DateTime dateTime;

    public GoogleTime(DateTime dateTime){
        this.dateTime = dateTime;
    }

    public GoogleTime(ZonedDateTime dateTime){
        this.dateTime = toGoogleDateTime(dateTime);
    }

    public DateTime get() {
        return dateTime;
    }

    public ZonedDateTime getZoned() {
        return fromGoogleDateTime(dateTime);
    }

    private ZonedDateTime fromGoogleDateTime(DateTime dateTime) {
        return ZonedDateTime.parse(dateTime.toStringRfc3339(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    private DateTime toGoogleDateTime(ZonedDateTime dateTime) {
        return new DateTime(Date.from(dateTime.toInstant()));
    }
}
