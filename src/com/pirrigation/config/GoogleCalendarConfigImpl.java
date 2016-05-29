package com.pirrigation.config;

import com.typesafe.config.Config;

/**
 * Created by r4dx on 29.05.2016.
 */
public class GoogleCalendarConfigImpl implements GoogleCalendarConfig {
    private final String calendarId;
    private final String eventId;
    private final String secretJsonPath;
    private final String appName;

    public GoogleCalendarConfigImpl(Config config) {
        this.calendarId = config.getString("googleCalendar.calendarId");
        this.eventId = config.getString("googleCalendar.eventId");
        this.secretJsonPath = config.getString("googleCalendar.secretJsonPath");
        this.appName = config.getString("googleCalendar.appName");
    }

    @Override
    public String getCalendarId() {
        return calendarId;
    }

    @Override
    public String getEventId() {
        return eventId;
    }

    @Override
    public String getSecretJsonPath() {
        return secretJsonPath;
    }

    @Override
    public String getAppName() {
        return appName;
    }

    @Override
    public String toString() {
        return ObjectToString.toString(this);
    }
}
