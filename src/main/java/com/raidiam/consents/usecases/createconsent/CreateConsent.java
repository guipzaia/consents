package com.raidiam.consents.usecases.createconsent;

import com.raidiam.consents.adapters.repositories.IConsentRepository;
import com.raidiam.consents.domain.entities.Consent;
import com.raidiam.consents.usecases.createconsent.port.CreateConsentRequest;
import com.raidiam.consents.usecases.createconsent.port.CreateConsentResponse;
import com.raidiam.consents.utils.CustomFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.Date;

@Service
public class CreateConsent implements ICreateConsent {

    @Autowired
    private Clock clock;

    @Autowired
    private IConsentRepository consentRepository;

    @Override
    public CreateConsentResponse execute(CreateConsentRequest request) {

        String now = CustomFormatter.RFC3339.format(Date.from(clock.instant()));

        var consent = Consent.builder()
                .userId(request.getUserId())
                .status(request.getStatus())
                .permissions(request.getPermissions())
                .createdAt(now)
                .updatedAt(now)
                .build();

        consentRepository.save(consent);

        return CreateConsentResponse.builder()
                .consentId(consent.getFormattedConsentId())
                .userId(consent.getUserId())
                .permissions(consent.getPermissions())
                .status(consent.getStatus())
                .createdAt(consent.getCreatedAt())
                .updatedAt(consent.getUpdatedAt())
                .requestDateTime(now)
                .build();
    }
}
