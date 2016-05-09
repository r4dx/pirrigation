package com.pirrigation.config;

/**
 * Created by r4dx on 09.05.2016.
 */
public interface PirrigationServiceConfig {
    long getPumpWorkMs();

    long getCheckFrequencySeconds();

    int getPoolSize();

    String getPumpControlPin();

    String getCalendarId();

    String getEventId();

    String getGoogleClientSecretJsonPath();

    String getGoogleAppName();
}
