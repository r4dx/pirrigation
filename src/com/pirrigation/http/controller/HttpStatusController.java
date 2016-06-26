package com.pirrigation.http.controller;

import com.pirrigation.PirrigationService;
import com.pirrigation.http.controller.responses.StatusGetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HttpStatusController {
    private static final Logger logger = LoggerFactory.getLogger(HttpStatusController.class);

    private final PirrigationService service;

    public HttpStatusController(PirrigationService service) {
        this.service = service;
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public StatusGetResponse getStatus() {
        logger.info("/status GET");
        return new StatusGetResponse(service.getLastOccurrenceTime(), service.getNextEvent().getNextTime());
    }
}
