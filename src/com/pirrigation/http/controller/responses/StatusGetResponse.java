package com.pirrigation.http.controller.responses;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Created by r4dx on 26.06.2016.
 */
public class StatusGetResponse {
    private final ZonedDateTime lastOccurrence;
    private final ZonedDateTime nextEvent;

    public StatusGetResponse(ZonedDateTime lastOccurrence, ZonedDateTime nextEvent) {
        this.lastOccurrence = lastOccurrence;
        this.nextEvent = nextEvent;
    }

    public ZonedDateTime getLastTimeEventHappened() {
        return lastOccurrence;
    }

    public ZonedDateTime getNextEvent() {
        return nextEvent;
    }

    public Optional<Long> getSecondsToNextEvent() {
        if (nextEvent == null) {
            return Optional.empty();
        }
        return Optional.of(ChronoUnit.SECONDS.between(ZonedDateTime.now(), nextEvent));
    }

    public Optional<Long> getSecondsFromLastEvent() {
        if (lastOccurrence == null) {
            return Optional.empty();
        }
        return Optional.of(ChronoUnit.SECONDS.between(lastOccurrence, ZonedDateTime.now()));
    }
}
