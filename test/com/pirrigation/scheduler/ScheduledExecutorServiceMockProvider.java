package com.pirrigation.scheduler;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by r4dx on 07.05.2016.
 */
class ScheduledExecutorServiceMockProvider {

    public ScheduledExecutorService callCallbacksImmediatelyInsteadOfFixedDelay(
            int repeatTimes, Supplier<ScheduledFuture> futureSupplier) {
        ScheduledExecutorService service = mock(ScheduledExecutorService.class);
        when(service.scheduleWithFixedDelay(notNull(Runnable.class), anyLong(), anyLong(), any(TimeUnit.class)))
                .thenAnswer((invocation -> {
                    Runnable callback = ((Runnable)(invocation.getArguments()[0]));
                    for (int i = 0; i < repeatTimes; i++)
                        callback.run();

                    return futureSupplier == null ? mockFuture() : futureSupplier.get();
                }));
        return service;
    }

    public ScheduledFuture mockFuture() {
        ScheduledFuture result = getMockedFuture();
        when(result.cancel(anyBoolean())).thenReturn(true);
        return result;
    }

    private ScheduledFuture getMockedFuture() {
        ScheduledFuture result = mock(ScheduledFuture.class);
        when(result.getDelay(any())).thenReturn(1L);
        return result;
    }

    public ScheduledFuture mockFutureThatCannotBeCancelled() {
        ScheduledFuture result = getMockedFuture();
        when(result.cancel(anyBoolean())).thenReturn(false);
        return result;
    }

    public ScheduledFuture mockRunningFuture() {
        ScheduledFuture result = mock(ScheduledFuture.class);
        when(result.getDelay(any())).thenReturn(0L);
        return result;
    }
}
