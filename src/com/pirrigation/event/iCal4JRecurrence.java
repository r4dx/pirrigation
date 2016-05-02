package com.pirrigation.event;

import net.fortuna.ical4j.model.*;

import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 * Created by r4dx on 01.05.2016.
 */
public class iCal4JRecurrence implements Recurrence {

    private String recurrence;


    public iCal4JRecurrence(String recurrence) {
        this.recurrence = recurrence;
    }

    @Override
    public LocalDate getNextDate(ZonedDateTime startDateTime) {
        Recur recur;
        try {
            recur = new Recur(recurrence);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        ZonedDateTime fromDate = ZonedDateTime.now();

//        if (startDateTime.isAfter(ZonedDateTime.now()))
//            fromDate = fromDate.minusDays(1);
        
        Date date = recur.getNextDate(toiCal4JDate(startDateTime), toiCal4JDate(fromDate));
        return fromiCal4JDate(date).toLocalDate();
    }

    private DateTime toiCal4JDate(ZonedDateTime date) {
        final String zoneId = "UTC";
        final String pattern = "yyyy-MM-dd HH:mm:ss";
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        ZonedDateTime unifiedDate = date.withZoneSameInstant(ZoneId.of(zoneId));
        TimeZone zone = new TimeZoneRegistryImpl().getTimeZone(zoneId);

        String dateStr = formatter.format(unifiedDate);

        try {

            return new DateTime(dateStr, pattern, zone);
        } catch (ParseException e) {
            throw new RuntimeException();
        }

/*
        DateTime result = new DateTime(new java.util.Date(Instant.ofEpochSecond(date.toEpochSecond()).toEpochMilli()));


        result.setTimeZone(zone);
        return result;
*/
    }

    private ZonedDateTime fromiCal4JDate(Date date) {
        return ZonedDateTime.ofInstant(date.toInstant(), ZoneId.of("UTC"));
    }
}
