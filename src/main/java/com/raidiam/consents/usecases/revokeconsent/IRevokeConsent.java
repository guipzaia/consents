package com.raidiam.consents.usecases.revokeconsent;

import com.raidiam.consents.usecases.revokeconsent.port.RevokeConsentRequest;

public interface IRevokeConsent {
    void execute(RevokeConsentRequest request);
}
