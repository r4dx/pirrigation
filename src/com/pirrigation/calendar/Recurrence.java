package com.pirrigation.calendar;

import java.time.LocalDate;

/**
 * Created by r4dx on 01.05.2016.
 */
public interface Recurrence {
    LocalDate getNextDate(LocalDate startDate);
}
