package com.technokratos.agona.api;

import com.technokratos.agona.api.dto.TaskRequestDto;
import com.technokratos.agona.api.dto.TaskResponseDto;
import com.technokratos.agona.api.dto.TaskUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Task | Задания", description = "Таск")
@RequestMapping("/api/v1/tasks")
public interface TaskApi {

    @Operation(summary = "Получить задание",
            description = "Возвращает задание по ID",
            operationId = "getTaskById",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешно",
                            content = @Content(schema = @Schema(implementation = TaskResponseDto.class))
                    )
            })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    TaskResponseDto getTaskById(@PathVariable("id") UUID id);


    @Operation(summary = "Получить все задания",
            description = "Возвращет все задания из БД",
            operationId = "getAllTasks",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешно",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskResponseDto.class)))
                    )
            })
    @GetMapping()
    List<TaskResponseDto> getAllTasks();


    @Operation(
            summary = "Создать задание",
            description = "Создает новое задание",
            operationId = "createTask",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Задание успешно создано",
                            content = @Content(schema = @Schema(implementation = UUID.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные данные"
                    )
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UUID create(@RequestBody TaskRequestDto taskRequestDto);


    @Operation(
            summary = "Обновить задание",
            description = "Обновляет существующее задание по ID",
            operationId = "updateTask",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Задание успешно обновлено",
                            content = @Content(schema = @Schema(implementation = TaskResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные данные"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Задание не найдено"
                    )
            }
    )
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    TaskResponseDto update(
            @PathVariable("id") UUID id,
            @RequestBody TaskUpdateDto taskUpdateDto
    );


    @Operation(
            summary = "Удалить задание",
            description = "Удаляет задание по ID",
            operationId = "deleteTask",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Задание успешно удалено"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Задание не найдено"
                    )
            }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable("id") UUID id);

}
