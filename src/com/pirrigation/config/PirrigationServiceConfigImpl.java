package com.pirrigation.config;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

/**
 * Created by r4dx on 09.05.2016.
 */
public class PirrigationServiceConfigImpl implements PirrigationServiceConfig {
    @Override
    public long getPumpWorkMs() {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public long getCheckFrequencySeconds() {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public int getPoolSize() {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public String getPumpControlPin() {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public String getCalendarId() {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public String getEventId() {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public String getGoogleClientSecretJsonPath() {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public String getGoogleAppName() {
        throw new RuntimeException("Not Implemented");
    }
}
