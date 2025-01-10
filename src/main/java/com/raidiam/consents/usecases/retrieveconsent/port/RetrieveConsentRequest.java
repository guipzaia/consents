package com.raidiam.consents.usecases.retrieveconsent.port;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RetrieveConsentRequest {

    @Pattern(regexp = "consent-\\d+", message = "Field consentId must have the pattern 'consent-N' (N = number)")
    private String consentId;
}
