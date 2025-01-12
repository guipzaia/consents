package com.raidiam.consents.adapters.rest;

import com.raidiam.consents.adapters.rest.port.ConsentRequest;
import com.raidiam.consents.adapters.rest.port.ConsentResponse;
import com.raidiam.consents.adapters.rest.port.ConsentUpdateRequest;
import com.raidiam.consents.domain.enums.ConsentPermission;
import com.raidiam.consents.domain.enums.ConsentStatus;
import com.raidiam.consents.domain.exceptions.ConsentPermissionsWithDuplicateValueException;
import com.raidiam.consents.usecases.createconsent.ICreateConsent;
import com.raidiam.consents.usecases.createconsent.port.CreateConsentRequest;
import com.raidiam.consents.usecases.createconsent.port.CreateConsentResponse;
import com.raidiam.consents.usecases.retrieveconsent.IRetrieveConsent;
import com.raidiam.consents.usecases.retrieveconsent.port.RetrieveConsentRequest;
import com.raidiam.consents.usecases.retrieveconsent.port.RetrieveConsentResponse;
import com.raidiam.consents.usecases.revokeconsent.IRevokeConsent;
import com.raidiam.consents.usecases.revokeconsent.port.RevokeConsentRequest;
import com.raidiam.consents.usecases.updateconsent.IUpdateConsent;
import com.raidiam.consents.usecases.updateconsent.port.UpdateConsentRequest;
import com.raidiam.consents.usecases.updateconsent.port.UpdateConsentResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.raidiam.consents.domain.messages.ErrorMessage.DUPLICATE_PERMISSIONS_DETECTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConsentControllerTest {

    @InjectMocks
    ConsentController consentController;

    @Mock
    private ICreateConsent createConsent;

    @Mock
    private IRetrieveConsent retrieveConsent;

    @Mock
    private IUpdateConsent updateConsent;

    @Mock
    private IRevokeConsent revokeConsent;

    @Test
    public void createConsentSuccessfully() {

        // Arrange
        var frozenTime = "2025-01-01T00:00:00Z";

        var mockCreateConsentResponse =
                CreateConsentResponse.builder()
                        .consentId("consent-1")
                        .userId("user-12345")
                        .permissions(List.of(ConsentPermission.READ_DATA))
                        .status(ConsentStatus.AWAITING_AUTHORISATION)
                        .createdAt(frozenTime)
                        .updatedAt(frozenTime)
                        .requestDateTime(frozenTime)
                        .build();

        when(createConsent.execute(any(CreateConsentRequest.class))).thenReturn(mockCreateConsentResponse);

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
                        .createdAt(frozenTime)
                        .updatedAt(frozenTime)
                        .meta(ConsentResponse.Meta.builder()
                                .requestDateTime(frozenTime)
                                .build())
                        .build();

        // Act
        var createdConsent = consentController.createConsent(consentRequest);

        // Assert
        assertThat(createdConsent)
                .usingRecursiveComparison()
                .isEqualTo(expectedConsentResponse);
    }

    @Test
    public void tryCreateConsentWithDuplicatePermissions() {

        // Arrange
        var consentRequest =
                ConsentRequest.builder()
                        .userId("user-12345")
                        .permissions(List.of(ConsentPermission.READ_DATA, ConsentPermission.READ_DATA))
                        .status(ConsentStatus.AWAITING_AUTHORISATION)
                        .build();

        // Act
        var exception = assertThrows(ConsentPermissionsWithDuplicateValueException.class,
                () -> consentController.createConsent(consentRequest)
        );

        // Assert
        assertEquals(DUPLICATE_PERMISSIONS_DETECTED, exception.getLocalizedMessage());
    }

    @Test
    public void retrieveConsentSuccessfully() {

        // Arrange
        var frozenTime = "2025-01-01T00:00:00Z";

        var mockRetrieveConsentResponse =
                RetrieveConsentResponse.builder()
                        .consentId("consent-1")
                        .userId("user-12345")
                        .permissions(List.of(ConsentPermission.READ_DATA))
                        .status(ConsentStatus.AWAITING_AUTHORISATION)
                        .createdAt(frozenTime)
                        .updatedAt(frozenTime)
                        .requestDateTime(frozenTime)
                        .build();

        when(retrieveConsent.execute(any(RetrieveConsentRequest.class))).thenReturn(mockRetrieveConsentResponse);

        var expectedConsentResponse =
                ConsentResponse.builder()
                        .consentId("consent-1")
                        .userId("user-12345")
                        .permissions(List.of(ConsentPermission.READ_DATA))
                        .status(ConsentStatus.AWAITING_AUTHORISATION)
                        .createdAt(frozenTime)
                        .updatedAt(frozenTime)
                        .meta(ConsentResponse.Meta.builder()
                                .requestDateTime(frozenTime)
                                .build())
                        .build();

        // Act
        var retrievedConsent = consentController.retrieveConsent("consent-1");

        // Assert
        assertThat(retrievedConsent)
                .usingRecursiveComparison()
                .isEqualTo(expectedConsentResponse);
    }

    @Test
    public void updateConsentSuccessfully() {

        // Arrange
        var frozenTime = "2025-01-01T00:00:00Z";

        var consentUpdateRequest =
                ConsentUpdateRequest.builder()
                        .permissions(List.of(
                            ConsentPermission.READ_DATA,
                            ConsentPermission.WRITE_DATA
                        ))
                        .status(ConsentStatus.AUTHORISED)
                        .build();

        var mockUpdateConsentResponse =
                UpdateConsentResponse.builder()
                        .consentId("consent-1")
                        .userId("user-12345")
                        .permissions(List.of(
                                ConsentPermission.READ_DATA,
                                ConsentPermission.WRITE_DATA
                        ))
                        .status(ConsentStatus.AUTHORISED)
                        .createdAt(frozenTime)
                        .updatedAt(frozenTime)
                        .requestDateTime(frozenTime)
                        .build();

        when(updateConsent.execute(any(UpdateConsentRequest.class))).thenReturn(mockUpdateConsentResponse);

        var expectedConsentResponse =
                ConsentResponse.builder()
                        .consentId("consent-1")
                        .userId("user-12345")
                        .permissions(List.of(
                                ConsentPermission.READ_DATA,
                                ConsentPermission.WRITE_DATA
                        ))
                        .status(ConsentStatus.AUTHORISED)
                        .createdAt(frozenTime)
                        .updatedAt(frozenTime)
                        .meta(ConsentResponse.Meta.builder()
                                .requestDateTime(frozenTime)
                                .build())
                        .build();

        // Act
        var updatedConsent = consentController.updateConsent("consent-1", consentUpdateRequest);

        // Assert
        assertThat(updatedConsent)
                .usingRecursiveComparison()
                .isEqualTo(expectedConsentResponse);
    }

    @Test
    public void tryUpdateConsentWithDuplicatePermissions() {

        // Arrange
        var consentUpdateRequest =
                ConsentUpdateRequest.builder()
                        .permissions(List.of(ConsentPermission.READ_DATA, ConsentPermission.READ_DATA))
                        .status(ConsentStatus.AWAITING_AUTHORISATION)
                        .build();

        // Act
        var exception = assertThrows(ConsentPermissionsWithDuplicateValueException.class,
                () -> consentController.updateConsent("consent-12345", consentUpdateRequest)
        );

        // Assert
        assertEquals(DUPLICATE_PERMISSIONS_DETECTED, exception.getLocalizedMessage());
    }

    @Test
    public void revokeConsentSuccessfully() {

        // Arrange & Act
        consentController.revokeConsent("consent-1");

        // Assert
        verify(revokeConsent, times(1)).execute(any(RevokeConsentRequest.class));
    }
}
