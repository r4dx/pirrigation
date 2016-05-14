package com.pirrigation.providers;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.calendar.Calendar;
import com.pirrigation.config.PirrigationServiceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by r4dx on 09.05.2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ GoogleCalendarModule.class, Calendar.Builder.class,
        GoogleCredential.class, GoogleNetHttpTransport.class, NetHttpTransport.class })
public class GoogleCalendarModuleTest {
    private GoogleCalendarModule module = new GoogleCalendarModule();

    @Test
    public void testProvideCalendarDoesNotThrowException() throws Exception {
        Calendar.Builder mockedBuilder = mock(Calendar.Builder.class);
        when(mockedBuilder.setApplicationName(anyString())).thenReturn(mockedBuilder);
        whenNew(Calendar.Builder.class).withAnyArguments().thenReturn(mockedBuilder);
        whenNew(FileInputStream.class).withAnyArguments().thenReturn(mock(FileInputStream.class));

        mockStatic(GoogleCredential.class);
        GoogleCredential mockedCredential = mock(GoogleCredential.class);
        when(GoogleCredential.fromStream(any(InputStream.class))).thenReturn(mockedCredential);
        when(mockedCredential.createScoped(any(Collection.class))).thenReturn(mockedCredential);


        mockStatic(GoogleNetHttpTransport.class);
        when(GoogleNetHttpTransport.newTrustedTransport()).thenReturn(mock(NetHttpTransport.class));

        module.provideCalendar(mock(PirrigationServiceConfig.class));
    }

    @Test
    public void testConfigureDoesNotThrowException() {
        module.configure();
    }

}
