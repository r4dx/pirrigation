package com.pirrigation.scheduler;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by r4dx on 30.07.2016.
 */
public class FutureUtilsTest {
    @Test
    public void testTrue() {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
        ScheduledFuture future = service.schedule(() -> { while (true); }, 1, TimeUnit.NANOSECONDS);
        Assert.assertTrue(new FutureUtils().isFutureRunning(future));
    }

    @Test
    public void testFalse() {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
        ScheduledFuture future = service.schedule(() -> { }, 1, TimeUnit.NANOSECONDS);
        Sleeper.DEFAULT.sleep(100);
        Assert.assertFalse(new FutureUtils().isFutureRunning(future));
    }
}
