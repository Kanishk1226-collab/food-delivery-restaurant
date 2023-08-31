package com.example.food.delivery.Validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TimeFormatValidator implements ConstraintValidator<ValidTimeFormat, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Add your time format validation logic here
        // For example, use a regular expression to validate the format
//        return value.matches("^(0[1-9]|1[0-2]):[0-5][0-9] (AM|PM)$");
        return value.matches("^(?:[01]\\d|2[0-3]):[0-5]\\d$");
    }
}