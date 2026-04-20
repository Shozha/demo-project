package com.technokratos.agona.controller.handler;

import com.technokratos.agona.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ExceptionMessage handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ExceptionMessage.builder()
                .message(ex.getMessage())
                .exceptionName(ex.getClass().getSimpleName())
                .build();
    }

    @ExceptionHandler(ServiceException.class)
    public final ResponseEntity<ExceptionMessage> handleServiceException(ServiceException exception) {
        return ResponseEntity.status(exception.getHttpStatus())
                .body(ExceptionMessage.builder()
                        .exceptionName(exception.getClass().getSimpleName())
                        .message(exception.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ExceptionMessage onAllExceptions(Exception exception) {
        return ExceptionMessage.builder()
                .message(exception.getMessage())
                .exceptionName(exception.getClass().getSimpleName())
                .build();
    }

}
