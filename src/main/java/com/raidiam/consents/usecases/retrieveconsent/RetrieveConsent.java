package com.raidiam.consents.usecases.retrieveconsent;

import com.raidiam.consents.adapters.repositories.IConsentRepository;
import com.raidiam.consents.domain.exceptions.ConsentNotFoundException;
import com.raidiam.consents.usecases.createconsent.CreateConsent;
import com.raidiam.consents.usecases.retrieveconsent.port.RetrieveConsentRequest;
import com.raidiam.consents.usecases.retrieveconsent.port.RetrieveConsentResponse;
import com.raidiam.consents.utils.CustomFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.Date;

import static com.raidiam.consents.utils.ErrorMessage.CONSENT_NOT_FOUND;

@Service
public class RetrieveConsent implements IRetrieveConsent {

    @Autowired
    private Clock clock;

    @Autowired
    private IConsentRepository consentRepository;

    private final Logger logger = LoggerFactory.getLogger(RetrieveConsent.class);

    /**
     * Retrieve details of a specific consent
     *
     * @param request                       Consent retrieve data
     * @return RetrieveConsentResponse      Consent details
     */
    @Override
    public RetrieveConsentResponse execute(RetrieveConsentRequest request) {

        logger.info("Retrieve consent request: {}", request);

        // Get current timestamp according to RFC3339 UTC
        String now = CustomFormatter.RFC3339.format(Date.from(clock.instant()));

        // Get numerical consent id
        Long consentId = CustomFormatter.getLongConsentId(request.getConsentId());

        // Try to find consent in repository
        var consent = consentRepository.findById(consentId).orElseThrow(() -> new ConsentNotFoundException(CONSENT_NOT_FOUND));

        var retrieveConsentResponse =
                RetrieveConsentResponse.builder()
                    .consentId(consent.getFormattedConsentId())
                    .userId(consent.getUserId())
                    .permissions(consent.getPermissions())
                    .status(consent.getStatus())
                    .createdAt(consent.getCreatedAt())
                    .updatedAt(consent.getUpdatedAt())
                    .requestDateTime(now)
                    .build();

        logger.info("Consent retrieved successfully: {}", retrieveConsentResponse);

        return retrieveConsentResponse;
    }
}
