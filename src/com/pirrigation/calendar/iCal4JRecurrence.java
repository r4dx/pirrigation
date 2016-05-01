package com.pirrigation.calendar;

import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.Recur;
import net.fortuna.ical4j.util.TimeZones;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Created by r4dx on 01.05.2016.
 */
public class iCal4JRecurrence implements Recurrence {

    private String recurrence;


    public iCal4JRecurrence(String recurrence) {
        this.recurrence = recurrence;
    }

    @Override
    public LocalDate getNextDate(LocalDate startDate) {
        Recur recur;
        try {
            recur = new Recur(recurrence);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Date date = recur.getNextDate(localDateToiCal4JDate(startDate), new Date());
        return iCal4JDateToLocalDate(date);
    }

    private Date localDateToiCal4JDate(LocalDate date) {
        return new Date(date.atStartOfDay().toInstant(ZoneOffset.UTC).getEpochSecond());
    }

    private LocalDate iCal4JDateToLocalDate(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), TimeZones.getUtcTimeZone().toZoneId()).toLocalDate();
    }
}
