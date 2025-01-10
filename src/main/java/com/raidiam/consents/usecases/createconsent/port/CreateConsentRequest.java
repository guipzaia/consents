package com.raidiam.consents.usecases.createconsent.port;

import com.raidiam.consents.domain.enums.ConsentPermission;
import com.raidiam.consents.domain.enums.ConsentStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CreateConsentRequest {
    private String userId;
    private List<ConsentPermission> permissions;
    private ConsentStatus status;
}
