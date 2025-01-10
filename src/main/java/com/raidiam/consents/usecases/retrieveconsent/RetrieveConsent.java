package com.raidiam.consents.usecases.retrieveconsent;

import com.raidiam.consents.adapters.repositories.IConsentRepository;
import com.raidiam.consents.domain.exceptions.ConsentNotFoundException;
import com.raidiam.consents.usecases.retrieveconsent.port.RetrieveConsentRequest;
import com.raidiam.consents.usecases.retrieveconsent.port.RetrieveConsentResponse;
import com.raidiam.consents.utils.CustomFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.Date;

@Service
public class RetrieveConsent implements IRetrieveConsent {

    @Autowired
    private Clock clock;

    @Autowired
    private IConsentRepository consentRepository;

    @Override
    public RetrieveConsentResponse execute(RetrieveConsentRequest request) {

        String now = CustomFormatter.RFC3339.format(Date.from(clock.instant()));
        Long consentId = CustomFormatter.getLongConsentId(request.getConsentId());

        var consent = consentRepository.findById(consentId).orElseThrow(() -> new ConsentNotFoundException("Consent not found"));

        return RetrieveConsentResponse.builder()
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
