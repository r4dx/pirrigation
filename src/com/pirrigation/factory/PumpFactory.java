package com.pirrigation.factory;

import com.pi4j.io.gpio.GpioController;
import com.pirrigation.config.PumpConfig;
import com.pirrigation.scheduler.Sleeper;
import com.pirrigation.water.Pump;
import com.pirrigation.water.StubPump;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Created by r4dx on 11.06.2016.
 */
@Configuration
public class PumpFactory {
    @Bean
    @Scope("singleton")
    public Pump pump(Sleeper sleeper, PumpConfig config, GpioController controller) {
        return new StubPump(sleeper);
        //return new PiPump(controller, config.getControlPin(), sleeper);
    }

    @Bean
    @Scope("singleton")
    public GpioController gpioController() {
        return Mockito.mock(GpioController.class);
        //return GpioFactory.getInstance();
    }

    @Bean
    @Scope("singleton")
    public Sleeper sleeper() {
        return Sleeper.DEFAULT;
    }

}
