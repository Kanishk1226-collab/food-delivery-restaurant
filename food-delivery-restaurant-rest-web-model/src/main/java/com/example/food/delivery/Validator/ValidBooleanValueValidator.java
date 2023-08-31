package com.example.food.delivery.Validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidBooleanValueValidator implements ConstraintValidator<ValidBooleanValue, Boolean> {

    @Override
    public boolean isValid(Boolean value, ConstraintValidatorContext context) {
        return value != null && (value == true || value == false);
    }
}