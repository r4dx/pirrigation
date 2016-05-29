package com.pirrigation.config;

import java.time.Duration;

/**
 * Created by r4dx on 29.05.2016.
 */
public interface SchedulerConfig {
    Duration getCheckFrequency();
    int getPoolSize();
}
