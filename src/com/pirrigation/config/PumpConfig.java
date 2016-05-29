package com.pirrigation.config;

import com.pi4j.io.gpio.Pin;

import java.time.Duration;

/**
 * Created by r4dx on 29.05.2016.
 */
public interface PumpConfig {
    Duration getWorkDuration();
    Pin getControlPin();
}
