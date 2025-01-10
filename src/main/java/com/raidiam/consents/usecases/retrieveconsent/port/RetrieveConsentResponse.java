package com.raidiam.consents.usecases.retrieveconsent.port;

import com.raidiam.consents.domain.enums.ConsentPermission;
import com.raidiam.consents.domain.enums.ConsentStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class RetrieveConsentResponse {

    private String consentId;
    private String userId;
    private List<ConsentPermission> permissions;
    private ConsentStatus status;
    private String createdAt;
    private String updatedAt;
    private String requestDateTime;
}
