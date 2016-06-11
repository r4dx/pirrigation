package com.pirrigation.http.controller.responses;

import java.time.Duration;

/**
 * Created by r4dx on 11.06.2016.
 */
public class PumpPostResponse {
    private final Duration duration;

    public PumpPostResponse(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }
}
