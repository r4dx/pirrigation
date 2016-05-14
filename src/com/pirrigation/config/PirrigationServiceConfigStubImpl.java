package com.pirrigation.config;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

/**
 * Created by r4dx on 09.05.2016.
 */
public class PirrigationServiceConfigStubImpl implements PirrigationServiceConfig {

    // This block of constants will go to configuration
    private final long PUMP_WORK_MS = 10000;
    private final long CHECK_FREQUENCY_SECONDS = 30;
    private final int POOL_SIZE = 10;
    private final Pin PUMP_CONTROL_PIN = RaspiPin.GPIO_25;
    private final String CALENDAR_ID = "magicforesterrors@gmail.com";
    private final String EVENT_ID = "coqjgpho6cs6ab9i68pjeb9kc9h64b9o68sjab9ockoj2ob469j38p1i68";
    private final String GOOGLE_CLIENT_SECRET_JSON_PATH = "conf/google_client_secret.json";
    private final String GOOGLE_APP_NAME = "Pirrigation";

    @Override
    public long getPumpWorkMs() {
        return PUMP_WORK_MS;
    }

    @Override
    public long getCheckFrequencySeconds() {
        return CHECK_FREQUENCY_SECONDS;
    }

    @Override
    public int getPoolSize() {
        return POOL_SIZE;
    }

    @Override
    public String getPumpControlPin() {
        return PUMP_CONTROL_PIN.getName();
    }

    @Override
    public String getCalendarId() {
        return CALENDAR_ID;
    }

    @Override
    public String getEventId() {
        return EVENT_ID;
    }

    @Override
    public String getGoogleClientSecretJsonPath() {
        return GOOGLE_CLIENT_SECRET_JSON_PATH;
    }

    @Override
    public String getGoogleAppName() {
        return GOOGLE_APP_NAME;
    }
}
