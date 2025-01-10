package com.raidiam.consents.usecases.createconsent;

import com.raidiam.consents.usecases.createconsent.port.CreateConsentRequest;
import com.raidiam.consents.usecases.createconsent.port.CreateConsentResponse;

public interface ICreateConsent {
    CreateConsentResponse execute(CreateConsentRequest request);
}
