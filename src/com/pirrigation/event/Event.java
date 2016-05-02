package com.pirrigation.event;

import java.time.ZonedDateTime;

public interface Event {
    ZonedDateTime getNextTime();
}
