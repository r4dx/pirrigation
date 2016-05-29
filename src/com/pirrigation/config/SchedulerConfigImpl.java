package com.pirrigation.config;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.typesafe.config.Config;

import java.time.Duration;

/**
 * Created by r4dx on 29.05.2016.
 */
public class SchedulerConfigImpl implements SchedulerConfig {
    private final Duration checkFrequency;
    private final int poolSize;

    public SchedulerConfigImpl(Config config) {
        this.checkFrequency = config.getDuration("googleCalendar.scheduler.checkFrequency");
        this.poolSize = config.getInt("googleCalendar.scheduler.poolSize");
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
    public String toString() {
        return ObjectToString.toString(this);
    }
}
