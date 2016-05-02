package com.pirrigation.event;

import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * Created by r4dx on 01.05.2016.
 */
public interface Recurrence {
    LocalDate getNextDate(ZonedDateTime startDateTime);
}
