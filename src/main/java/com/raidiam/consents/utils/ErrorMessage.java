package com.raidiam.consents.utils;

public class ErrorMessage {

    public static final String REQUIRED_CONSENT_ID_PATH_PARAMETER = "Path parameter consentId is required";
    public static final String INVALID_CONSENT_ID_PATH_PARAMETER_PATTERN = "Path parameter consentId must have the pattern 'consent-N' (N = number)";
    public static final String DUPLICATE_PERMISSIONS_DETECTED = "Duplicate permissions detected";
    public static final String INVALID_CONSENT_INITIAL_STATUS = "Initial status of consent must be AWAITING_AUTHORISATION";
    public static final String INVALID_CONSENT_UPDATE_STATUS = "Status AWAITING_AUTHORISATION is not allowed for update consent";
    public static final String INVALID_VALUE_IN_THE_LIST = "Field %s must be one of the values in the list %s";
    public static final String CONSENT_NOT_FOUND = "Consent not found";
    public static final String INVALID_INPUT = "Invalid input";
    public static final String INTERNAL_SERVER_ERROR = "Internal server error";
    public static final String TOO_MANY_REQUESTS = "Too many requests";

}
