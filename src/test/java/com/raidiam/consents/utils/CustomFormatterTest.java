package com.raidiam.consents.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CustomFormatterTest {

    @Test
    public void getLongConsentIdSuccessfully() {

        // Arrange & Act
        var longConsentId = CustomFormatter.getLongConsentId("consent-12345");

        // Assert
        assertEquals(12345L, longConsentId);
    }
}
