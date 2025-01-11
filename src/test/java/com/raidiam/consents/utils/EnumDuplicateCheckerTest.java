package com.raidiam.consents.utils;

import com.raidiam.consents.domain.enums.ConsentStatus;
import org.checkerframework.checker.units.qual.C;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(MockitoJUnitRunner.class)
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
