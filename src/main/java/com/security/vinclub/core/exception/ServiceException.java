package com.security.vinclub.core.exception;

import com.security.vinclub.core.response.ErrorData;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceException extends RuntimeException {

    private final HttpStatus statusCode;
    private final String errorCode;
    private final Object error;
    private final ErrorData errorMapping;

    public ServiceException(HttpStatus statusCode, Throwable cause, Object error, String errCode, String errMessage, ErrorData errorMapping, Object... args){
        super(String.format(errMessage, args), cause);
        this.statusCode = statusCode;
        this.errorCode = errCode;
        this.error = error;
        this.errorMapping = errorMapping;
    }

    public ServiceException(HttpStatus statusCode, Throwable cause, Object error, String errCode, String errMessage, Object... args){
        super(String.format(errMessage, args), cause);
        this.statusCode = statusCode;
        this.errorCode = errCode;
        this.error = error;
        this.errorMapping = null;
    }

    public ServiceException(HttpStatus statusCode, ErrorData errorData){
        super(errorData.getMsg());
        this.statusCode = statusCode;
        this.errorCode = errorData.getCode();
        this.error = errorData.getError();
        this.errorMapping = errorData;
    }
}
