package com.raidiam.consents.adapters.validators.consentpermission;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = ConsentPermissionValidator.class)
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConsentPermissionConstraint {
    String message() default "Invalid permission";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
