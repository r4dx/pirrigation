package com.pirrigation.water;

import com.pirrigation.scheduler.Sleeper;

/**
 * Created by r4dx on 09.05.2016.
 */
public class StubPump implements Pump {

    private Sleeper sleeper;

    public StubPump(Sleeper sleeper) {
        this.sleeper = sleeper;
    }

    @Override
    public void start(long workMs) {
        sleeper.sleep(workMs);
    }
}
