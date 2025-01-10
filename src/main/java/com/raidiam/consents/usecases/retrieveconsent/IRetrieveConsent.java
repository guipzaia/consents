package com.raidiam.consents.usecases.retrieveconsent;

import com.raidiam.consents.usecases.retrieveconsent.port.RetrieveConsentRequest;
import com.raidiam.consents.usecases.retrieveconsent.port.RetrieveConsentResponse;

public interface IRetrieveConsent {
    RetrieveConsentResponse execute(RetrieveConsentRequest request);
}
