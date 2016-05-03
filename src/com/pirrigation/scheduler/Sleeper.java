package com.pirrigation.scheduler;

/**
 * Created by r4dx on 03.05.2016.
 */
public interface Sleeper {
    void sleep(long millis);

    Sleeper DEFAULT = millis -> {
            try {
                Thread.sleep(millis);
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
}
