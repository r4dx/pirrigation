package com.pirrigation.http.controller;

import com.pirrigation.http.controller.responses.PumpPostResponse;
import com.pirrigation.water.Pump;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.Duration;

@RestController
public class HttpPumpController {
    private static final Logger logger = LoggerFactory.getLogger(HttpPumpController.class);

    private final Pump pump;

    public HttpPumpController(Pump pump) {
        this.pump = pump;
    }

    @RequestMapping(value = "/pump", method = RequestMethod.POST)
    public PumpPostResponse startPump(@RequestParam(value = "duration") Duration duration) {
        logger.info("/pump POST: {}", duration);
        pump.start(duration);
        return new PumpPostResponse(duration);
    }
}
