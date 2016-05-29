package com.pirrigation.providers;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.pirrigation.config.GoogleCalendarConfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

/**
 * Created by r4dx on 09.05.2016.
 */
public class GoogleCalendarModule extends AbstractModule {
    @Override
    protected void configure() {

    }

    @Provides
    public Calendar provideCalendar(GoogleCalendarConfig config) throws GeneralSecurityException, IOException {
        try (InputStream is = new FileInputStream(config.getSecretJsonPath())) {
            return getCalendarService(config, is);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Calendar getCalendarService(GoogleCalendarConfig config,
                                        InputStream clientSecretJson) throws GeneralSecurityException, IOException {
        Calendar.Builder builder = new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
                getCredential(clientSecretJson));
        return builder.setApplicationName(config.getAppName()).build();
    }

    private Credential getCredential(InputStream clientSecretJson) throws IOException {
        return GoogleCredential.fromStream(clientSecretJson).createScoped(
                Collections.singletonList(CalendarScopes.CALENDAR_READONLY));

    }
}
