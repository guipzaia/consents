package com.raidiam.consents.adapters.rest;

import com.raidiam.consents.adapters.rest.port.ConsentRequest;
import com.raidiam.consents.adapters.rest.port.ConsentResponse;
import com.raidiam.consents.domain.enums.ConsentPermission;
import com.raidiam.consents.domain.enums.ConsentStatus;
import com.raidiam.consents.usecases.createconsent.ICreateConsent;
import com.raidiam.consents.usecases.createconsent.port.CreateConsentRequest;
import com.raidiam.consents.usecases.createconsent.port.CreateConsentResponse;
import com.raidiam.consents.usecases.retrieveconsent.IRetrieveConsent;
import com.raidiam.consents.usecases.revokeconsent.IRevokeConsent;
import com.raidiam.consents.usecases.updateconsent.IUpdateConsent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.awaitility.Awaitility.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConsentControllerTest {

    @InjectMocks
    private ConsentController consentController;

    @Mock
    private ICreateConsent createConsent;

    @Mock
    private IRetrieveConsent retrieveConsent;

    @Mock
    private IUpdateConsent updateConsent;

    @Mock
    private IRevokeConsent revokeConsent;

    @Test
    public void createConsentSuccessfullyTest() {

        // Arrange
        var mockCreateConsentRequest =
                CreateConsentRequest.builder()
                        .userId("user-12345")
                        .permissions(List.of(ConsentPermission.READ_DATA))
                        .status(ConsentStatus.AWAITING_AUTHORISATION)
                        .build();

        var mockCreateConsentResponse =
                CreateConsentResponse.builder()
                        .consentId("consent-1")
                        .userId("user-12345")
                        .permissions(List.of(ConsentPermission.READ_DATA))
                        .status(ConsentStatus.AWAITING_AUTHORISATION)
                        .createdAt("2025-01-01T00:00:00Z")
                        .updatedAt("2025-01-01T00:00:00Z")
                        .requestDateTime("2025-01-01T00:00:00Z")
                        .build();

        lenient().doReturn(mockCreateConsentResponse).when(createConsent).execute(mockCreateConsentRequest);

        var consentRequest =
                ConsentRequest.builder()
                        .userId("user-12345")
                        .permissions(List.of(ConsentPermission.READ_DATA))
                        .status(ConsentStatus.AWAITING_AUTHORISATION)
                        .build();

        var expectedConsentResponse =
                ConsentResponse.builder()
                        .consentId("consent-1")
                        .userId("user-12345")
                        .permissions(List.of(ConsentPermission.READ_DATA))
                        .status(ConsentStatus.AWAITING_AUTHORISATION)
                        .createdAt("2025-01-01T00:00:00Z")
                        .updatedAt("2025-01-01T00:00:00Z")
                        .meta(ConsentResponse.Meta.builder()
                                .requestDateTime("2025-01-01T00:00:00Z")
                                .build())
                        .build();

        // Act
        var consentResponse = consentController.createConsent(consentRequest);

        // Assert
        assertEquals(consentResponse, expectedConsentResponse);
    }
}
