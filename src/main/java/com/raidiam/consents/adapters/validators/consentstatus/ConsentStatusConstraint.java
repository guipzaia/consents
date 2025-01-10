package com.raidiam.consents.adapters.validators.consentstatus;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = ConsentStatusValidator.class)
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConsentStatusConstraint {
    String message() default "Invalid status";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
