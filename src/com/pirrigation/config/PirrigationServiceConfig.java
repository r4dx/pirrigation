package com.pirrigation.config;

import com.pi4j.io.gpio.Pin;

import java.time.Duration;

/**
 * Created by r4dx on 09.05.2016.
 */
public interface PirrigationServiceConfig {
    Duration getPumpWorkDuration();

    Duration getCheckFrequency();

    int getPoolSize();

    Pin getPumpControlPin();

    String getCalendarId();

    String getEventId();

    String getGoogleClientSecretJsonPath();

    String getGoogleAppName();
}
