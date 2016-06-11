package com.pirrigation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PirrigationApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(PirrigationApp.class);
        PirrigationService service = context.getBean(PirrigationService.class);
        service.serve();
    }
}