package com.raidiam.consents.usecases;

import com.raidiam.consents.adapters.repositories.IConsentRepository;
import com.raidiam.consents.domain.entities.Consent;
import com.raidiam.consents.domain.enums.ConsentPermission;
import com.raidiam.consents.domain.enums.ConsentStatus;
import com.raidiam.consents.domain.exceptions.ConsentNotFoundException;
import com.raidiam.consents.usecases.retrieveconsent.RetrieveConsent;
import com.raidiam.consents.usecases.retrieveconsent.port.RetrieveConsentRequest;
import com.raidiam.consents.usecases.retrieveconsent.port.RetrieveConsentResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static com.raidiam.consents.domain.messages.ErrorMessage.CONSENT_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RetrieveConsentTest {

    @Mock
    Clock clock;

    @Mock
    IConsentRepository consentRepository;

    @InjectMocks
    RetrieveConsent retrieveConsent;

    @Test
    public void retrieveConsentSuccessfully() {

        // Arrange
        var frozenTime = LocalDateTime.of(2025, 1, 1, 3, 0, 0).toInstant(ZoneOffset.UTC);
        var frozenTimeString = "2025-01-01T00:00:00Z";

        var savedMockConsent =
                Consent.builder()
                        .consentId(1L)
                        .userId("user-12345")
                        .status(ConsentStatus.AWAITING_AUTHORISATION)
                        .permissions(List.of(ConsentPermission.READ_DATA))
                        .createdAt(frozenTimeString)
                        .updatedAt(frozenTimeString)
                        .build();

        var retrieveConsentRequest =
                RetrieveConsentRequest.builder()
                        .consentId("consent-1")
                        .build();

        var expectedConsentResponse =
                RetrieveConsentResponse.builder()
                        .consentId("consent-1")
                        .userId("user-12345")
                        .status(ConsentStatus.AWAITING_AUTHORISATION)
                        .permissions(List.of(ConsentPermission.READ_DATA))
                        .createdAt(frozenTimeString)
                        .updatedAt(frozenTimeString)
                        .requestDateTime(frozenTimeString)
                        .build();

        when(clock.instant()).thenReturn(frozenTime);
        when(consentRepository.findById(1L)).thenReturn(Optional.ofNullable(savedMockConsent));

        // Act
        var retrievedConsent = retrieveConsent.execute(retrieveConsentRequest);

        // Assert
        assertThat(retrievedConsent).usingRecursiveComparison().isEqualTo(expectedConsentResponse);
    }

    @Test
    public void tryRetrieveConsentWithNotFoundIdentification() {

        // Arrange
        var retrieveConsentRequest =
                RetrieveConsentRequest.builder()
                        .consentId("consent-1")
                        .build();

        // Act
        var exception = assertThrows(ConsentNotFoundException.class,
                () -> retrieveConsent.execute(retrieveConsentRequest)
        );

        // Assert
        assertEquals(CONSENT_NOT_FOUND, exception.getLocalizedMessage());
    }
}
