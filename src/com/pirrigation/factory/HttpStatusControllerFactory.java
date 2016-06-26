package com.pirrigation.factory;

import com.pirrigation.PirrigationService;
import com.pirrigation.http.controller.HttpStatusController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by r4dx on 11.06.2016.
 */
@Configuration
public class HttpStatusControllerFactory {
    @Bean
    public HttpStatusController httpStatusController(PirrigationService service) {
        return new HttpStatusController(service);
    }
}
