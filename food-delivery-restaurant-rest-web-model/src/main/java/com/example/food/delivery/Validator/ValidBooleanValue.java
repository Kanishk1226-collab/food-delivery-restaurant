package com.example.food.delivery.Validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidBooleanValueValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBooleanValue {
    String message() default "Invalid boolean value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
