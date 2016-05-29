package com.pirrigation.water;

import com.pirrigation.scheduler.Sleeper;
import org.junit.Test;

import java.time.Duration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by r4dx on 09.05.2016.
 */
public class StubPumpTest {
    @Test
    public void testStarted() {
        Sleeper sleeper = mock(Sleeper.class);
        Pump pump = new StubPump(sleeper);
        final Duration waitDuration = Duration.ofMillis(1000);
        pump.start(waitDuration);
        verify(sleeper).sleep(waitDuration.toMillis());
    }
}
