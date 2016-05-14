package com.pirrigation;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.pirrigation.providers.EventModule;
import com.pirrigation.providers.GoogleCalendarModule;
import com.pirrigation.providers.PirrigationServiceModule;
import org.slf4j.LoggerFactory;

class PirrigationApp {
    public static void main(String[] args) {
        initLogback();

        Injector injector = Guice.createInjector(new PirrigationServiceModule(), new EventModule(),
                new GoogleCalendarModule());
        PirrigationService service = injector.getInstance(PirrigationService.class);
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