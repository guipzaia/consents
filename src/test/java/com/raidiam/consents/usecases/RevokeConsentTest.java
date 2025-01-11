package com.raidiam.consents.usecases;

import com.raidiam.consents.adapters.repositories.IConsentRepository;
import com.raidiam.consents.domain.entities.Consent;
import com.raidiam.consents.domain.exceptions.ConsentNotFoundException;
import com.raidiam.consents.usecases.revokeconsent.RevokeConsent;
import com.raidiam.consents.usecases.revokeconsent.port.RevokeConsentRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static com.raidiam.consents.domain.messages.ErrorMessage.CONSENT_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RevokeConsentTest {

    @Mock
    IConsentRepository consentRepository;

    @InjectMocks
    private RevokeConsent revokeConsent;


    @Test
    public void revokeConsentSuccessfully() {

        // Arrange
        var revokeConsentRequest =
                RevokeConsentRequest.builder()
                        .consentId("consent-1")
                        .build();

        when(consentRepository.findById(1L)).thenReturn(Optional.ofNullable(Consent.builder().build()));
        doNothing().when(consentRepository).deleteById(1L);

        // Act
        revokeConsent.execute(revokeConsentRequest);

        // Assert
        verify(consentRepository, times(1)).findById(1L);
        verify(consentRepository, times(1)).deleteById(1L);
    }

    @Test
    public void tryRevokeConsentWithNotFoundIdentification() {

        // Arrange
        var revokeConsentRequest =
                RevokeConsentRequest.builder()
                        .consentId("consent-1")
                        .build();

        // Act
        var exception = assertThrows(ConsentNotFoundException.class,
                () -> revokeConsent.execute(revokeConsentRequest)
        );

        // Assert
        assertEquals(CONSENT_NOT_FOUND, exception.getLocalizedMessage());
    }
}
