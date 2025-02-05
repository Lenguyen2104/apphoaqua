package com.security.vinclub.core.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.vinclub.core.response.ErrorData;
import com.security.vinclub.core.response.ResponseBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.security.vinclub.core.response.ResponseStatus.FAILURE;


@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    protected ResponseEntity<Object> handleControlledException(ServiceException ex, NativeWebRequest request) {
        var status = ex.getStatusCode();

        var newErrorData = ex.getErrorMapping() != null ? new ObjectMapper().convertValue(ex.getErrorMapping(), ErrorData.class) : null;
        var errorKey1 = "";
        var errorKey2 = "";

        if (null != newErrorData) {
            errorKey1 = newErrorData.getErrorKey1();
            errorKey2 = newErrorData.getErrorKey2();
        }

        var response = new ResponseBody<>();
        response.setOperationFail(FAILURE, ErrorData.builder()
                .code(ex.getErrorCode())
                .msg(ex.getMessage())
                .error(ex.getError())
                .cause(ex.getCause() != null ? ex.getCause().toString() : null)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .errorKey1(errorKey1)
                .errorKey2(errorKey2)
                .build());

        return handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
    }
}
