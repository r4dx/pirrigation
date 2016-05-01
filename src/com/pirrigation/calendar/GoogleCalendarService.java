package com.pirrigation.calendar;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Arrays;

/**
 * Created by r4dx on 01.05.2016.
 */
public class GoogleCalendarService {
    private final HttpTransport httpTransport;
    private InputStream clientSecretJson;
    private String applicationName;

    public GoogleCalendarService(InputStream clientSecretJson, String applicationName) throws GeneralSecurityException,
            IOException {
        this.clientSecretJson = clientSecretJson;
        this.applicationName = applicationName;
        this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    }

    public Calendar get() throws IOException {
        return new com.google.api.services.calendar.Calendar.Builder(
                httpTransport, JacksonFactory.getDefaultInstance(), getCredential())
                .setApplicationName(applicationName)
                .build();
    }

    private Credential getCredential() throws IOException {
        return GoogleCredential.fromStream(clientSecretJson).createScoped(
                Arrays.asList(CalendarScopes.CALENDAR_READONLY));

    }
}
