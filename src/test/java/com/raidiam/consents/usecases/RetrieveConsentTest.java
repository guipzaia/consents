package com.raidiam.consents.usecases;

import com.raidiam.consents.adapters.repositories.IConsentRepository;
import com.raidiam.consents.domain.entities.Consent;
import com.raidiam.consents.domain.enums.ConsentPermission;
import com.raidiam.consents.domain.enums.ConsentStatus;
import com.raidiam.consents.domain.exceptions.ConsentNotFoundException;
import com.raidiam.consents.usecases.retrieveconsent.RetrieveConsent;
import com.raidiam.consents.usecases.retrieveconsent.port.RetrieveConsentRequest;
import com.raidiam.consents.usecases.retrieveconsent.port.RetrieveConsentResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.raidiam.consents.domain.messages.ErrorMessage.CONSENT_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
        var frozenDateTime = LocalDateTime.of(2025, 1, 1, 3, 0, 0);
        var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        var formattedFrozenDateTime = dateTimeFormatter.format(frozenDateTime);
        var instant = ZonedDateTime.of(frozenDateTime, ZoneId.systemDefault()).toInstant();

        var savedMockConsent =
                Consent.builder()
                        .consentId(1L)
                        .userId("user-12345")
                        .status(ConsentStatus.AWAITING_AUTHORISATION)
                        .permissions(List.of(ConsentPermission.READ_DATA))
                        .createdAt(formattedFrozenDateTime)
                        .updatedAt(formattedFrozenDateTime)
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
                        .createdAt(formattedFrozenDateTime)
                        .updatedAt(formattedFrozenDateTime)
                        .requestDateTime(formattedFrozenDateTime)
                        .build();

        when(clock.instant()).thenReturn(instant);
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
