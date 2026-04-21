package com.technokratos.agona.exception.file;

import com.technokratos.agona.exception.ServiceException;
import org.springframework.http.HttpStatus;

public class FileStorageException extends ServiceException {

    public FileStorageException(String message, Throwable cause) {
        super(message, cause, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
