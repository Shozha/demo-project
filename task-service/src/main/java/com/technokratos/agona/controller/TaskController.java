package com.technokratos.agona.controller;

import com.technokratos.agona.api.TaskApi;
import com.technokratos.agona.api.dto.TaskRequestDto;
import com.technokratos.agona.api.dto.TaskResponseDto;
import com.technokratos.agona.api.dto.TaskUpdateDto;
import com.technokratos.agona.exception.ServiceException;
import com.technokratos.agona.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TaskController implements TaskApi {

    private final TaskService taskService;

    @Override
    public TaskResponseDto getTaskById(UUID id) {
        return taskService.getById(id);
    }

    @Override
    public List<TaskResponseDto> getAllTasks() {
        return taskService.getAll();
    }

    @Override
    public UUID create(TaskRequestDto taskRequestDto) {
        return taskService.create(taskRequestDto);
    }

    @Override
    public TaskResponseDto update(UUID id, TaskUpdateDto taskUpdateDto) {
        return taskService.update(id, taskUpdateDto);
    }

    @Override
    public void delete(UUID id) {
        taskService.delete(id);
    }
}
