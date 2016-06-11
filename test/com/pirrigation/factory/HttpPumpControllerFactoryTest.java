package com.pirrigation.factory;

import com.pirrigation.water.Pump;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * Created by r4dx on 11.06.2016.
 */
public class HttpPumpControllerFactoryTest {
    @Test
    public void httpPumpControllerDoesNotThrowException() {
        HttpPumpControllerFactory factory = new HttpPumpControllerFactory();
        factory.httpPumpController(mock(Pump.class));
    }
}
