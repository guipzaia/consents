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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.raidiam.consents.domain.messages.ErrorMessage.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
                        .createdAt(formattedFrozenDateTime)
                        .updatedAt(formattedFrozenDateTime)
                        .requestDateTime(formattedFrozenDateTime)
                        .build();

        when(clock.instant()).thenReturn(instant);
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

    @Test
    public void tryUpdateConsentLongIdentification() {

        // Arrange
        var updateConsentRequest =
                UpdateConsentRequest.builder()
                        .consentId("consent-99999999999999999999")
                        .build();

        // Act
        var exception = assertThrows(Exception.class,
                () -> updateConsent.execute(updateConsentRequest)
        );

        // Assert
        assertTrue(exception.getLocalizedMessage().contains("99999999999999999999"));
    }
}
