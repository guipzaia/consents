package com.raidiam.consents.utils;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class CustomFormatter {
    public static final SimpleDateFormat RFC3339 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public static Long getLongConsentId(String consentId) {
        return Long.valueOf(consentId.replaceAll("\\D+", ""));
    }
}
