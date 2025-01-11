package com.raidiam.consents.usecases.revokeconsent;

import com.raidiam.consents.adapters.repositories.IConsentRepository;
import com.raidiam.consents.domain.exceptions.ConsentNotFoundException;
import com.raidiam.consents.usecases.revokeconsent.port.RevokeConsentRequest;
import com.raidiam.consents.utils.CustomFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.raidiam.consents.domain.messages.ErrorMessage.CONSENT_NOT_FOUND;

@Service
public class RevokeConsent implements IRevokeConsent {

    @Autowired
    private IConsentRepository consentRepository;

    private final Logger logger = LoggerFactory.getLogger(RevokeConsent.class);

    /**
     * Revoke consent
     *
     * @param request   Consent revoke data
     */
    @Override
    public void execute(RevokeConsentRequest request) {

        logger.info("Revoke consent request: {}", request);

        // Get numerical consent id
        Long consentId = CustomFormatter.getLongConsentId(request.getConsentId());

        // Try to find consent in repository
        consentRepository.findById(consentId).orElseThrow(() -> new ConsentNotFoundException(CONSENT_NOT_FOUND));

        consentRepository.deleteById(consentId);

        logger.info("Consent revoked successfully: {}", request);
    }
}
