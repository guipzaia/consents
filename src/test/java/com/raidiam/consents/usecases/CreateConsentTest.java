package com.raidiam.consents.usecases;

import com.raidiam.consents.adapters.repositories.IConsentRepository;
import com.raidiam.consents.domain.entities.Consent;
import com.raidiam.consents.domain.enums.ConsentPermission;
import com.raidiam.consents.domain.enums.ConsentStatus;
import com.raidiam.consents.domain.exceptions.ConsentWithInvalidStatusException;
import com.raidiam.consents.usecases.createconsent.CreateConsent;
import com.raidiam.consents.usecases.createconsent.port.CreateConsentRequest;
import com.raidiam.consents.usecases.createconsent.port.CreateConsentResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static com.raidiam.consents.domain.messages.ErrorMessage.INVALID_CONSENT_INITIAL_STATUS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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
                        .createdAt(frozenTimeString)
                        .updatedAt(frozenTimeString)
                        .requestDateTime(frozenTimeString)
                        .build();

        when(clock.instant()).thenReturn(frozenTime);
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
