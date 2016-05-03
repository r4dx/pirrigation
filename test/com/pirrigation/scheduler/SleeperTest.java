package com.pirrigation.scheduler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;

/**
 * Created by r4dx on 03.05.2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Sleeper.class})
public class SleeperTest {

    @Test(expected = RuntimeException.class)
    public void testSleep() throws InterruptedException {
        PowerMockito.mockStatic(Thread.class);
        PowerMockito.doThrow(new InterruptedException()).when(Thread.class);
        // this initialize the stub in powermock
        Thread.sleep(anyLong());
        Sleeper.DEFAULT.sleep(anyLong());
    }
}
