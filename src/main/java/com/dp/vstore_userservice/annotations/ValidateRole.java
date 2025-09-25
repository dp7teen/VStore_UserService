package com.dp.vstore_userservice.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidateRoleValidator.class)
public @interface ValidateRole {

    Class<? extends Enum<?>> enumClass();

    String message() default "Role must be one of {enumClass}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
