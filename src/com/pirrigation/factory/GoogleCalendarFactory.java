package com.pirrigation.factory;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.pirrigation.config.GoogleCalendarConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

/**
 * Created by r4dx on 11.06.2016.
 */
@Configuration
public class GoogleCalendarFactory {

    @Bean
    public Calendar calendar(GoogleCalendarConfig config) throws GeneralSecurityException, IOException {
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
