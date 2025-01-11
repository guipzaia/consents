package com.raidiam.consents.utils;

import java.text.SimpleDateFormat;

public class CustomFormatter {

    /**
     *  RFC339 UTC datetime formatter
     */
    public static final SimpleDateFormat RFC3339 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    /**
     * Return numerical consent identification
     *
     * @param consentId     String consent identification
     * @return Long         Numerical consent identification
     */
    public static Long getLongConsentId(String consentId) {
        return Long.valueOf(consentId.replaceAll("\\D+", ""));
    }
}
