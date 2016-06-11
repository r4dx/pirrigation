package com.pirrigation.factory;

import com.pirrigation.http.controller.HttpPumpController;
import com.pirrigation.scheduler.Sleeper;
import com.pirrigation.water.Pump;
import com.pirrigation.water.StubPump;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Created by r4dx on 11.06.2016.
 */
@Configuration
public class HttpPumpControllerFactory {
    @Bean
    public HttpPumpController httpPumpController(Pump pump) {
        return new HttpPumpController(pump);
    }
}
