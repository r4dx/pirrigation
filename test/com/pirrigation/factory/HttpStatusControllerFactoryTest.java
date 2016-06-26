package com.pirrigation.factory;

import com.pirrigation.PirrigationService;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * Created by r4dx on 11.06.2016.
 */
public class HttpStatusControllerFactoryTest {
    @Test
    public void httpStatusControllerDoesNotThrowException() {
        HttpStatusControllerFactory factory = new HttpStatusControllerFactory();
        factory.httpStatusController(mock(PirrigationService.class));
    }
}
