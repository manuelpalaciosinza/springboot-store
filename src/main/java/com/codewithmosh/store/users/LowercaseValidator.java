package com.codewithmosh.store.users;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LowercaseValidator implements ConstraintValidator<Lowercase,String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) return true;
        return s.equals(s.toLowerCase());
    }
}
