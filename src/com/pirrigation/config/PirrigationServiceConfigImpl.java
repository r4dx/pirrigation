package com.pirrigation.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.typesafe.config.Config;

import java.time.Duration;

/**
 * Created by r4dx on 09.05.2016.
 */
public class PirrigationServiceConfigImpl implements PirrigationServiceConfig {

    private final Duration pumpWorkDuration;
    private final Duration checkFrequency;
    private final int poolSize;
    private final Pin pumpControlPin;
    private final String calendarId;
    private final String eventId;
    private final String googleClientSecretJsonPath;
    private final String googleAppName;

    public PirrigationServiceConfigImpl(Config config){
        this.pumpWorkDuration = config.getDuration("pumpWorkDuration");
        this.checkFrequency = config.getDuration("checkFrequency");
        this.poolSize = config.getInt("poolSize");
        this.pumpControlPin = RaspiPin.getPinByName(config.getString("pumpControlPin"));
        this.calendarId = config.getString("calendarId");
        this.eventId = config.getString("eventId");
        this.googleClientSecretJsonPath = config.getString("googleClientSecretJsonPath");
        this.googleAppName = config.getString("googleAppName");
    }

    public Duration getPumpWorkDuration() {
        return pumpWorkDuration;
    }

    @Override
    public Duration getCheckFrequency() {
        return checkFrequency;
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

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
