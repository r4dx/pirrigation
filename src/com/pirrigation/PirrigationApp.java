package com.pirrigation;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PirrigationApp {
    public static void main(String[] args) {
        initLogback();
        ConfigurableApplicationContext context = SpringApplication.run(PirrigationApp.class);
        PirrigationService service = context.getBean(PirrigationService.class);
        service.serve();
    }

    private static void initLogback() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);

            context.reset();
            configurator.doConfigure("conf/logback.xml");
        } catch (JoranException je) {
            // Will be caught in printInCaseOfErrorsOrWarnings but still just in case.
            System.err.println("Error reading logback.xml");
        }
        StatusPrinter.printInCaseOfErrorsOrWarnings(context);
    }
}