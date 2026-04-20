package com.technokratos.agona.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(
        description = "Модель ответа с информацией о задании",
        title = "TaskResponse",
        example = """
        {
            "id": "123e4567-e89b-12d3-a456-426614174000",
            "title": "AGONA-1",
            "description": "Реализовать функционал авторизации",
            "starterCode": "public class Solution { // code here }",
            "deadline": "2024-12-31T23:59:59",
            "reviewDeadline": "2025-01-07T23:59:59",
            "maxScore": 100,
            "createdAt": "2024-03-31T10:30:00Z"
        }
        """
)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDto {

    @Schema(
            description = "Уникальный идентификатор задания",
            type = "string",
            format = "uuid",
            example = "123e4567-e89b-12d3-a456-426614174000",
            required = true,
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private UUID id;

    @Schema(
            description = "Название/код задания",
            type = "string",
            example = "AGONA-1",
            required = true,
            minLength = 3,
            maxLength = 50
    )
    private String title;

    @Schema(
            description = "Подробное описание задания",
            type = "string",
            example = "Реализовать функционал авторизации с использованием JWT токенов",
            required = true,
            maxLength = 2000
    )
    private String description;

    @Schema(
            description = "Начальный код для выполнения задания",
            type = "string",
            example = "public class Solution {\n    public static void main(String[] args) {\n        // напишите ваше решение здесь\n    }\n}",
            nullable = true
    )
    private String starterCode;

    @Schema(
            description = "Дедлайн сдачи задания",
            type = "string",
            format = "date-time",
            example = "2024-12-31T23:59:59",
            required = true
    )
    private LocalDateTime deadline;

    @Schema(
            description = "Дедлайн проверки задания",
            type = "string",
            format = "date-time",
            example = "2025-01-07T23:59:59",
            required = true
    )
    private LocalDateTime reviewDeadline;

    @Schema(
            description = "Максимальный балл за задание",
            type = "integer",
            format = "int32",
            example = "100",
            required = true,
            minimum = "1",
            maximum = "100"
    )
    private Integer maxScore;

    @Schema(
            description = "Дата и время создания задания",
            type = "string",
            format = "date-time",
            example = "2024-03-31T10:30:00Z",
            required = true,
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime createdAt;
}