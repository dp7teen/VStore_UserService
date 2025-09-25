package com.dp.vstore_userservice.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ValidateRoleValidator implements ConstraintValidator<ValidateRole, List<String>> {
    private List<String> roles;

    @Override
    public void initialize(ValidateRole annotation) {
        roles = Arrays.stream(annotation.enumClass().getEnumConstants())
                .map(Enum::name).collect(Collectors.toList());
    }

    @Override
    public boolean isValid(List<String> values, ConstraintValidatorContext constraintValidatorContext) {
        if (values.isEmpty()) return false;
        return values.stream()
                .map(String::toUpperCase)
                .allMatch(roles::contains);
    }
}
