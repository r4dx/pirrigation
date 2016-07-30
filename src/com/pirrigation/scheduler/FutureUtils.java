package com.pirrigation.scheduler;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by r4dx on 30.07.2016.
 */
public class FutureUtils {
    public boolean isFutureRunning(ScheduledFuture<?> future) {
        return future.getDelay(TimeUnit.NANOSECONDS) <= 0 && !future.isDone();
    }
}
