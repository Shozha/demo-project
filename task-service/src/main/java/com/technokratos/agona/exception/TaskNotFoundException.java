package com.technokratos.agona.exception;

import java.util.UUID;

public class TaskNotFoundException extends NotFoundException {
    public TaskNotFoundException(UUID id) {
        super(String.format("Task with this id = %s, not found", id));
    }
}
