package com.app.login.common.utils;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

/**
 * ValidationFacade
 * @authr megadotnet
 * @date 2022/5/22
 */
@Component
public class ValidationFacade {

    private final Validator validator;

    public ValidationFacade(Validator validator) {
        this.validator = validator;
    }

    public <T> void validate(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> violations = validator.validate(object, groups);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}