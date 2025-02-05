package com.security.vinclub.exception;


import com.security.vinclub.core.exception.ServiceException;
import com.security.vinclub.core.response.ErrorData;
import com.security.vinclub.core.response.ResponseStatus;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.HttpStatus;

import java.util.Set;
import java.util.stream.Collectors;

public class ServiceSecurityException extends ServiceException {

    public ServiceSecurityException(HttpStatus statusCode, String errCode, String errMessage, ErrorData errorMapping, Object... args) {
        super(statusCode, null, null, errCode, errMessage, errorMapping, args);
    }

    public ServiceSecurityException(HttpStatus statusCode, ErrorData errorData) {
        super(statusCode, errorData);
    }

    public ServiceSecurityException(HttpStatus statusCode, ResponseStatus status, Object... args){
        super(statusCode, null, null, status.getCode(), status.getVieMsg(), args);
    }

    public ServiceSecurityException(Set<? extends ConstraintViolation<?>> violations) {
        super(HttpStatus.BAD_REQUEST,
                null,
                null,
                ResponseStatus.INVALID_REQUEST_PARAMETER.getCode(),
                violations != null ? ResponseStatus.INVALID_REQUEST_PARAMETER.getMessage() + renderText(violations) : null);
    }

    private static String renderText(Set<? extends ConstraintViolation<?>> violations) {
        var text = violations.stream()
                .map(v -> v == null ? "null" : String.format("'%s' %s", convertSnakeCase(v.getPropertyPath().toString()), v.getMessage()) + " |" )
                .collect(Collectors.joining(" "));

        return text.substring(0, text.length() - 2);
    }

    private static String convertSnakeCase(String str) {
        return str.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }
}