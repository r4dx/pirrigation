package com.pirrigation.http.controller;

import com.pirrigation.http.controller.responses.PumpPostResponse;
import com.pirrigation.water.Pump;
import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by r4dx on 11.06.2016.
 */
public class HttpPumpControllerTest {
    @Test
    public void testStartPump() {
        Pump pump = mock(Pump.class);
        Duration duration = Duration.ZERO;
        HttpPumpController controller = new HttpPumpController(pump);
        PumpPostResponse response = controller.startPump(duration);
        verify(pump).start(duration);
        Assert.assertEquals(duration, response.getDuration());
    }
}
