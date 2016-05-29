package com.pirrigation.config;

import com.pi4j.io.gpio.Pin;

/**
 * Created by r4dx on 09.05.2016.
 */
public interface PirrigationServiceConfig {
    long getPumpWorkMs();

    long getCheckFrequencySeconds();

    int getPoolSize();

    Pin getPumpControlPin();

    String getCalendarId();

    String getEventId();

    String getGoogleClientSecretJsonPath();

    String getGoogleAppName();
}
