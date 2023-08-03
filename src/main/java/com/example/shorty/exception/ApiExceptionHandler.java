package com.example.shorty.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = ApiBadRequestException.class)
    public ResponseEntity<Object> handleApiRequestException(ApiBadRequestException e) {
        final ApiException apiException = new ApiException();
        apiException.setMessage(e.getMessage());
        apiException.setHttpStatus(HttpStatus.BAD_REQUEST);
        apiException.setTimestamp(ZonedDateTime.now());

        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }

    @ExceptionHandler(value = ApiNotFoundException.class)
    public ResponseEntity<Object> handleApiNotFoundException(ApiNotFoundException e) {
        final ApiException apiException = new ApiException();
        apiException.setMessage(e.getMessage());
        apiException.setHttpStatus(HttpStatus.NOT_FOUND);
        apiException.setTimestamp(ZonedDateTime.now());

        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }

    @ExceptionHandler(value = ApiUnauthorizedException.class)
    public ResponseEntity<Object> handleApiUnauthorizedException(ApiUnauthorizedException e) {
        final ApiException apiException = new ApiException();
        apiException.setMessage(e.getMessage());
        apiException.setHttpStatus(HttpStatus.UNAUTHORIZED);
        apiException.setTimestamp(ZonedDateTime.now());

        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }
}
