package com.att.tdp.popcorn_palace.Exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

// this annotation is for specifying this class is a global exception handler
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    // function will handle exceptions specified in value
    // the rest of the exceptions will get handled by the default handlers in spring
    @ExceptionHandler(value = {IncorrectFieldException.class, ResourceAlreadyExistsException.class, ResourceNotFoundException.class})
    protected ResponseEntity<Object> handleIncorrectFieldException(Exception exception, WebRequest request) {
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("message", exception.getMessage());
        HttpStatus httpStatus;
        if (exception instanceof IncorrectFieldException) {
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (exception instanceof ResourceAlreadyExistsException) {
            httpStatus = HttpStatus.CONFLICT;
        } else if (exception instanceof ResourceNotFoundException) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return handleExceptionInternal(exception, responseMap, new HttpHeaders(), httpStatus, request);
    }
}