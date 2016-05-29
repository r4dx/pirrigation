package com.pirrigation.config;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.typesafe.config.Config;

/**
 * Created by r4dx on 09.05.2016.
 */
public class PirrigationServiceConfigImpl implements PirrigationServiceConfig {

    private final long pumpWorkMs;
    private final long checkFrequencySeconds;
    private final int poolSize;
    private final Pin pumpControlPin;
    private final String calendarId;
    private final String eventId;
    private final String googleClientSecretJsonPath;
    private final String googleAppName;

    public PirrigationServiceConfigImpl(Config config){
        this.pumpWorkMs = config.getLong("pumpWorkMs");
        this.checkFrequencySeconds = config.getLong("checkFrequencySeconds");
        this.poolSize = config.getInt("poolSize");
        this.pumpControlPin = RaspiPin.getPinByName(config.getString("pumpControlPin"));
        this.calendarId = config.getString("calendarId");
        this.eventId = config.getString("eventId");
        this.googleClientSecretJsonPath = config.getString("googleClientSecretJsonPath");
        this.googleAppName = config.getString("googleAppName");
    }

    @Override
    public long getPumpWorkMs() {
        return pumpWorkMs;
    }

    @Override
    public long getCheckFrequencySeconds() {
        return checkFrequencySeconds;
    }

    @Override
    public int getPoolSize() {
        return poolSize;
    }

    @Override
    public Pin getPumpControlPin() {
        return pumpControlPin;
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
    public String getGoogleClientSecretJsonPath() {
        return googleClientSecretJsonPath;
    }

    @Override
    public String getGoogleAppName() {
        return googleAppName;
    }
}
