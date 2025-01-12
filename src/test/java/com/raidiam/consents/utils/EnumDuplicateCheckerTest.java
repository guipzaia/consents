package com.raidiam.consents.utils;

import com.raidiam.consents.domain.enums.ConsentStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class EnumDuplicateCheckerTest {

    @Test
    public void shouldReturnTrueIfHasDuplicateItems() {
        assertTrue(EnumDuplicateChecker.hasDuplicateItems(List.of(ConsentStatus.AUTHORISED, ConsentStatus.AUTHORISED)));
    }

    @Test
    public void shouldReturnFalseIfNotHasDuplicateItems() {
        assertFalse(EnumDuplicateChecker.hasDuplicateItems(List.of(ConsentStatus.AUTHORISED, ConsentStatus.REJECT)));
    }
}
