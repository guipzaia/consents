package com.raidiam.consents.usecases.revokeconsent;

import com.raidiam.consents.adapters.repositories.IConsentRepository;
import com.raidiam.consents.domain.exceptions.ConsentNotFoundException;
import com.raidiam.consents.usecases.revokeconsent.port.RevokeConsentRequest;
import com.raidiam.consents.utils.CustomFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RevokeConsent implements IRevokeConsent {

    @Autowired
    private IConsentRepository consentRepository;

    @Override
    public void execute(RevokeConsentRequest request) {
        Long consentId = CustomFormatter.getLongConsentId(request.getConsentId());

        consentRepository.findById(consentId).orElseThrow(() -> new ConsentNotFoundException("Consent not found"));
        consentRepository.deleteById(consentId);
    }
}
