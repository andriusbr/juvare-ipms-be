package com.juvare.ipms.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionMapper {

    @ExceptionHandler(AuthorizationServiceException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationServiceException(AuthorizationServiceException exception){
        String errMsg = exception.getMessage();
        log.debug(errMsg, exception);
        ErrorResponse error = new ErrorResponse(errMsg);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
