package ru.kata.spring.boot_security.demo.configs.exception;

import org.springframework.validation.FieldError;
import ru.kata.spring.boot_security.demo.model.User;


import java.util.Collections;
import java.util.List;

public class UserValidationData {

    private final User user;
    private List<FieldError> fieldErrors;
    private final String message;

    public UserValidationData(User user, List<FieldError> fieldErrors, String message) {
        this(user, message);
        this.fieldErrors = fieldErrors;
    }

    public UserValidationData(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors == null ? Collections.emptyList() : fieldErrors;
    }

    public String getMessage() {
        return message;
    }
}
