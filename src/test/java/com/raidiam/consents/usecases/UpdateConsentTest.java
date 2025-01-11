package com.raidiam.consents.usecases;

import com.raidiam.consents.adapters.repositories.IConsentRepository;
import com.raidiam.consents.domain.entities.Consent;
import com.raidiam.consents.domain.enums.ConsentPermission;
import com.raidiam.consents.domain.enums.ConsentStatus;
import com.raidiam.consents.domain.exceptions.ConsentNotFoundException;
import com.raidiam.consents.domain.exceptions.ConsentWithInvalidStatusException;
import com.raidiam.consents.usecases.updateconsent.UpdateConsent;
import com.raidiam.consents.usecases.updateconsent.port.UpdateConsentRequest;
import com.raidiam.consents.usecases.updateconsent.port.UpdateConsentResponse;
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

import static com.raidiam.consents.domain.messages.ErrorMessage.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateConsentTest {

    @InjectMocks
    UpdateConsent updateConsent;

    @Mock
    Clock clock;

    @Mock
    IConsentRepository consentRepository;

    @Test
    public void updateConsentSuccessfully() {

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

        var updateConsentRequest =
                UpdateConsentRequest.builder()
                        .consentId("consent-1")
                        .status(ConsentStatus.AUTHORISED)
                        .permissions(List.of(
                            ConsentPermission.READ_DATA,
                            ConsentPermission.WRITE_DATA
                        ))
                        .build();

        var expectedConsentResponse =
                UpdateConsentResponse.builder()
                        .consentId("consent-1")
                        .userId("user-12345")
                        .status(ConsentStatus.AUTHORISED)
                        .permissions(List.of(
                                ConsentPermission.READ_DATA,
                                ConsentPermission.WRITE_DATA
                        ))
                        .createdAt(frozenTimeString)
                        .updatedAt(frozenTimeString)
                        .requestDateTime(frozenTimeString)
                        .build();

        when(clock.instant()).thenReturn(frozenTime);
        when(consentRepository.findById(1L)).thenReturn(Optional.ofNullable(savedMockConsent));

        // Act
        var updatedConsent = updateConsent.execute(updateConsentRequest);

        // Assert
        assertThat(updatedConsent).usingRecursiveComparison().isEqualTo(expectedConsentResponse);
    }

    @Test
    public void tryCreateConsentWithInvalidStatus() {

        // Arrange
        var updateConsentRequest =
                UpdateConsentRequest.builder()
                        .consentId("consent-1")
                        .status(ConsentStatus.AWAITING_AUTHORISATION)
                        .permissions(List.of(ConsentPermission.READ_DATA))
                        .build();

        // Act
        var exception = assertThrows(ConsentWithInvalidStatusException.class,
                () -> updateConsent.execute(updateConsentRequest)
        );

        // Assert
        assertEquals(INVALID_CONSENT_UPDATE_STATUS, exception.getLocalizedMessage());
    }

    @Test
    public void tryUpdateConsentWithNotFoundIdentification() {

        // Arrange
        var updateConsentRequest =
                UpdateConsentRequest.builder()
                        .consentId("consent-1")
                        .build();

        // Act
        var exception = assertThrows(ConsentNotFoundException.class,
                () -> updateConsent.execute(updateConsentRequest)
        );

        // Assert
        assertEquals(CONSENT_NOT_FOUND, exception.getLocalizedMessage());
    }
}
