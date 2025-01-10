package com.raidiam.consents.usecases.updateconsent;

import com.raidiam.consents.adapters.repositories.IConsentRepository;
import com.raidiam.consents.domain.entities.Consent;
import com.raidiam.consents.domain.exceptions.ConsentNotFoundException;
import com.raidiam.consents.usecases.retrieveconsent.IRetrieveConsent;
import com.raidiam.consents.usecases.retrieveconsent.port.RetrieveConsentResponse;
import com.raidiam.consents.usecases.updateconsent.port.UpdateConsentRequest;
import com.raidiam.consents.usecases.updateconsent.port.UpdateConsentResponse;
import com.raidiam.consents.utils.CustomFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.Date;

@Service
public class UpdateConsent implements IUpdateConsent {

    @Autowired
    private Clock clock;

    @Autowired
    private IConsentRepository consentRepository;

    @Override
    public UpdateConsentResponse execute(UpdateConsentRequest request) {

        String now = CustomFormatter.RFC3339.format(Date.from(clock.instant()));
        Long consentId = CustomFormatter.getLongConsentId(request.getConsentId());

        var consent = consentRepository.findById(consentId).orElseThrow(() -> new ConsentNotFoundException("Consent not found"));

        consent.setStatus(request.getStatus());
        consent.setPermissions(request.getPermissions());
        consent.setUpdatedAt(now);

        consentRepository.save(consent);

        return UpdateConsentResponse.builder()
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
