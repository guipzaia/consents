package com.raidiam.consents.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CustomFormatterTest {

    @Test
    public void getLongConsentIdSuccessfully() {

        // Arrange & Act
        var longConsentId = CustomFormatter.getLongConsentId("consent-12345");

        // Assert
        assertEquals(12345L, longConsentId);
    }
}
