package com.raidiam.consents.usecases.createconsent;

import com.raidiam.consents.adapters.repositories.IConsentRepository;
import com.raidiam.consents.domain.entities.Consent;
import com.raidiam.consents.domain.enums.ConsentStatus;
import com.raidiam.consents.domain.exceptions.ConsentWithInvalidStatusException;
import com.raidiam.consents.usecases.createconsent.port.CreateConsentRequest;
import com.raidiam.consents.usecases.createconsent.port.CreateConsentResponse;
import com.raidiam.consents.utils.CustomFormatter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.Date;

import static com.raidiam.consents.domain.messages.ErrorMessage.INVALID_CONSENT_INITIAL_STATUS;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class CreateConsent implements ICreateConsent {

    @Autowired
    private Clock clock;

    @Autowired
    private IConsentRepository consentRepository;

    private final Logger logger = LoggerFactory.getLogger(CreateConsent.class);

    /**
     * Create consent with initial status of AWAITING_AUTHORISATION
     *
     * @param request                   Consent create data
     * @return CreateConsentResponse    Created consent details
     */
    @Override
    public CreateConsentResponse execute(CreateConsentRequest request) {

        logger.info("Create consent request: {}", request);

        // Checks if initial status is AWAITING_AUTHORISATION
        if (! ConsentStatus.AWAITING_AUTHORISATION.equals(request.getStatus())) {
            throw new ConsentWithInvalidStatusException(INVALID_CONSENT_INITIAL_STATUS);
        }

        // Get current timestamp according to RFC3339 UTC
        String now = CustomFormatter.RFC3339.format(Date.from(clock.instant()));

        var consent = Consent.builder()
                .userId(request.getUserId())
                .status(request.getStatus())
                .permissions(request.getPermissions())
                .createdAt(now)
                .updatedAt(now)
                .build();

        var savedConsent = consentRepository.save(consent);

        var createConsentResponse =
                CreateConsentResponse.builder()
                    .consentId(savedConsent.getFormattedConsentId())
                    .userId(savedConsent.getUserId())
                    .permissions(savedConsent.getPermissions())
                    .status(savedConsent.getStatus())
                    .createdAt(savedConsent.getCreatedAt())
                    .updatedAt(savedConsent.getUpdatedAt())
                    .requestDateTime(now)
                    .build();

        logger.info("Consent created successfully: {}", createConsentResponse);

        return createConsentResponse;
    }
}
