package com.technokratos.agona.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(
        description = "DTO для обновления задания (поддерживает частичное обновление)",
        title = "TaskUpdate",
        example = """
        {
            "title": "AGONA-1-UPDATED",
            "description": "Обновленное описание задания",
            "starterCode": "public class Solution { // updated code }",
            "deadline": "2024-12-31T23:59:59",
            "reviewDeadline": "2025-01-07T23:59:59",
            "maxScore": 150
        }
        """
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateDto {

    @Schema(
            description = "Название/код задания",
            type = "string",
            example = "AGONA-1-UPDATED",
            minLength = 3,
            maxLength = 50,
            nullable = true
    )
    private String title;

    @Schema(
            description = "Подробное описание задания",
            type = "string",
            example = "Обновленное описание задания с дополнительными требованиями",
            maxLength = 2000,
            nullable = true
    )
    private String description;

    @Schema(
            description = "Начальный код для выполнения задания (шаблон)",
            type = "string",
            example = "public class Solution {\n    public static void main(String[] args) {\n        // обновленный код\n    }\n}",
            nullable = true
    )
    private String starterCode;

    @Schema(
            description = "Дедлайн сдачи задания",
            type = "string",
            format = "date-time",
            example = "2024-12-31T23:59:59",
            nullable = true
    )
    private LocalDateTime deadline;

    @Schema(
            description = "Дедлайн проверки задания",
            type = "string",
            format = "date-time",
            example = "2025-01-07T23:59:59",
            nullable = true
    )
    private LocalDateTime reviewDeadline;

    @Schema(
            description = "Максимальный балл за задание",
            type = "integer",
            format = "int32",
            example = "100",
            minimum = "1",
            maximum = "100",
            nullable = true
    )
    private Integer maxScore;
}