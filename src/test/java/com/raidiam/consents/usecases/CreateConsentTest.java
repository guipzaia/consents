package com.raidiam.consents.usecases;

import com.raidiam.consents.adapters.repositories.IConsentRepository;
import com.raidiam.consents.domain.entities.Consent;
import com.raidiam.consents.domain.enums.ConsentPermission;
import com.raidiam.consents.domain.enums.ConsentStatus;
import com.raidiam.consents.domain.exceptions.ConsentWithInvalidStatusException;
import com.raidiam.consents.usecases.createconsent.CreateConsent;
import com.raidiam.consents.usecases.createconsent.port.CreateConsentRequest;
import com.raidiam.consents.usecases.createconsent.port.CreateConsentResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.raidiam.consents.domain.messages.ErrorMessage.INVALID_CONSENT_INITIAL_STATUS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateConsentTest {

    @InjectMocks
    CreateConsent createConsent;

    @Mock
    Clock clock;

    @Mock
    IConsentRepository consentRepository;

    @Test
    public void createConsentSuccessfully() {

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

        var createConsentRequest =
                CreateConsentRequest.builder()
                        .userId("user-12345")
                        .status(ConsentStatus.AWAITING_AUTHORISATION)
                        .permissions(List.of(ConsentPermission.READ_DATA))
                        .build();

        var expectedConsentResponse =
                CreateConsentResponse.builder()
                        .consentId("consent-1")
                        .userId("user-12345")
                        .status(ConsentStatus.AWAITING_AUTHORISATION)
                        .permissions(List.of(ConsentPermission.READ_DATA))
                        .createdAt(formattedFrozenDateTime)
                        .updatedAt(formattedFrozenDateTime)
                        .requestDateTime(formattedFrozenDateTime)
                        .build();

        when(clock.instant()).thenReturn(instant);
        when(consentRepository.save(any(Consent.class))).thenReturn(savedMockConsent);

        // Act
        var createdConsent = createConsent.execute(createConsentRequest);

        // Assert
        assertThat(createdConsent).usingRecursiveComparison().isEqualTo(expectedConsentResponse);
    }

    @Test
    public void tryCreateConsentWithInvalidStatus() {

        // Arrange
        var createConsentRequest =
                CreateConsentRequest.builder()
                        .userId("user-12345")
                        .status(ConsentStatus.AUTHORISED)
                        .permissions(List.of(ConsentPermission.READ_DATA))
                        .build();

        // Act
        var exception = assertThrows(ConsentWithInvalidStatusException.class,
                () -> createConsent.execute(createConsentRequest)
        );

        // Assert
        assertEquals(INVALID_CONSENT_INITIAL_STATUS, exception.getLocalizedMessage());
    }
}
