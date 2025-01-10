package com.raidiam.consents.adapters.validators.consentpermission;

import com.raidiam.consents.domain.enums.ConsentPermission;
import com.raidiam.consents.domain.exceptions.ConsentNotFoundException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.EnumSet;
import java.util.Set;

public class ConsentPermissionValidator implements ConstraintValidator<ConsentPermissionConstraint, ConsentPermission> {

    private static final Set<ConsentPermission> PERMISSIONS = EnumSet.allOf(ConsentPermission.class);

    @Override
    public boolean isValid(ConsentPermission consentPermission, ConstraintValidatorContext constraintValidatorContext) {
        if (! PERMISSIONS.contains(consentPermission)) {
            throw new ConsentNotFoundException(String.format("The consent permission %s is not supported", consentPermission));
        }
        return true;
    }
}
