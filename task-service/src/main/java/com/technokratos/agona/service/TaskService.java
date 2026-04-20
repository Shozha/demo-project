package com.technokratos.agona.service;

import com.technokratos.agona.api.dto.TaskRequestDto;
import com.technokratos.agona.api.dto.TaskResponseDto;

import com.technokratos.agona.api.dto.TaskUpdateDto;
import com.technokratos.agona.exception.NotFoundException;
import com.technokratos.agona.exception.TaskNotFoundException;
import com.technokratos.agona.mapper.TaskMapper;
import com.technokratos.agona.model.Task;
import com.technokratos.agona.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskResponseDto getById(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return taskMapper.toDto(task);
    }

    public List<TaskResponseDto> getAll() {
        List<Task> tasks = taskRepository.findAll();
        return taskMapper.toDto(tasks);
    }

    public UUID create(TaskRequestDto taskRequestDto) {
        Task task = taskRepository.save(taskMapper.toEntity(taskRequestDto));
        return task.getId();
    }

    @Transactional
    public TaskResponseDto update(UUID id, TaskUpdateDto taskUpdateDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        taskMapper.updateEntity(taskUpdateDto, task);
        Task updateTask = taskRepository.save(task);
        return taskMapper.toDto(updateTask);
    }

    @Transactional
    public void delete(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.delete(task);
    }

}
