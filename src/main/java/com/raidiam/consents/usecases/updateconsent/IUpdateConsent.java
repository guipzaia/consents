package com.raidiam.consents.usecases.updateconsent;

import com.raidiam.consents.usecases.updateconsent.port.UpdateConsentRequest;
import com.raidiam.consents.usecases.updateconsent.port.UpdateConsentResponse;

public interface IUpdateConsent {
    UpdateConsentResponse execute(UpdateConsentRequest request);
}
