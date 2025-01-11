package com.raidiam.consents.usecases.updateconsent;

import com.raidiam.consents.adapters.repositories.IConsentRepository;
import com.raidiam.consents.domain.enums.ConsentStatus;
import com.raidiam.consents.domain.exceptions.ConsentNotFoundException;
import com.raidiam.consents.domain.exceptions.ConsentWithInvalidStatusException;
import com.raidiam.consents.usecases.updateconsent.port.UpdateConsentRequest;
import com.raidiam.consents.usecases.updateconsent.port.UpdateConsentResponse;
import com.raidiam.consents.utils.CustomFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.Date;

import static com.raidiam.consents.domain.messages.ErrorMessage.CONSENT_NOT_FOUND;
import static com.raidiam.consents.domain.messages.ErrorMessage.INVALID_CONSENT_UPDATE_STATUS;

@Service
public class UpdateConsent implements IUpdateConsent {

    @Autowired
    private Clock clock;

    @Autowired
    private IConsentRepository consentRepository;
    private final Logger logger = LoggerFactory.getLogger(UpdateConsent.class);

    /**
     * Update consent details
     *
     * @param request                   Consent update data
     * @return UpdateConsentResponse    Updated consent details
     */
    @Override
    public UpdateConsentResponse execute(UpdateConsentRequest request) {

        logger.info("Update consent request: {}", request);

        // Checks if new status is AUTHORISED or REJECT
        if (ConsentStatus.AWAITING_AUTHORISATION.equals(request.getStatus())) {
            throw new ConsentWithInvalidStatusException(INVALID_CONSENT_UPDATE_STATUS);
        }

        // Get numerical consent id
        Long consentId = CustomFormatter.getLongConsentId(request.getConsentId());

        // Try to find consent in repository
        var consent = consentRepository.findById(consentId).orElseThrow(() -> new ConsentNotFoundException(CONSENT_NOT_FOUND));

        // Get current timestamp according to RFC3339 UTC
        String now = CustomFormatter.RFC3339.format(Date.from(clock.instant()));

        // Set new details
        consent.setStatus(request.getStatus());
        consent.setPermissions(request.getPermissions());
        consent.setUpdatedAt(now);

        consentRepository.save(consent);

        var updateConsentResponse =
                UpdateConsentResponse.builder()
                    .consentId(consent.getFormattedConsentId())
                    .userId(consent.getUserId())
                    .permissions(consent.getPermissions())
                    .status(consent.getStatus())
                    .createdAt(consent.getCreatedAt())
                    .updatedAt(consent.getUpdatedAt())
                    .requestDateTime(now)
                    .build();

        logger.info("Consent updated successfully: {}", updateConsentResponse);

        return updateConsentResponse;
    }
}
