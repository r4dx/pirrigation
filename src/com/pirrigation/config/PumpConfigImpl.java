package com.pirrigation.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.typesafe.config.Config;

import java.time.Duration;

/**
 * Created by r4dx on 29.05.2016.
 */
public class PumpConfigImpl implements PumpConfig {

    private final Duration workDuration;
    private final Pin controlPin;

    public PumpConfigImpl(Config config) {
        this.workDuration = config.getDuration("pump.workDuration");
        this.controlPin = RaspiPin.getPinByName(config.getString("pump.controlPin"));
    }

    @Override
    public Duration getWorkDuration() {
        return workDuration;
    }

    @Override
    public Pin getControlPin() {
        return controlPin;
    }

    @Override
    public String toString() {
        return ObjectToString.toString(this);
    }
}
