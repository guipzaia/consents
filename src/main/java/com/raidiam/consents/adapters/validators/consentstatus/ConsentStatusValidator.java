package com.raidiam.consents.adapters.validators.consentstatus;

import com.raidiam.consents.domain.enums.ConsentPermission;
import com.raidiam.consents.domain.enums.ConsentStatus;
import com.raidiam.consents.domain.exceptions.ConsentNotFoundException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.EnumSet;
import java.util.Set;

public class ConsentStatusValidator implements ConstraintValidator<ConsentStatusConstraint, ConsentStatus> {

    private static final Set<ConsentStatus> PERMISSIONS = EnumSet.allOf(ConsentStatus.class);

    @Override
    public boolean isValid(ConsentStatus consentStatus, ConstraintValidatorContext constraintValidatorContext) {
        if (! PERMISSIONS.contains(consentStatus)) {
            throw new ConsentNotFoundException(String.format("The consent permission %s is not supported", consentStatus));
        }
        return true;
    }
}
