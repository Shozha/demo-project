package com.technokratos.agona.controller.handler;

import com.technokratos.agona.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ExceptionMessage> handleServiceException(ServiceException ex) {
        return ResponseEntity.status(ex.getHttpStatus())
                .body(ExceptionMessage.builder()
                        .time(LocalDateTime.now())
                        .exceptionName(ex.getClass().getSimpleName())
                        .message(ex.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionMessage> handleAllOtherExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionMessage.builder()
                        .time(LocalDateTime.now())
                        .exceptionName(ex.getClass().getSimpleName())
                        .message(String.format("Internal server error: %s",  ex.getMessage()))
                        .build()
                );
    }
}
