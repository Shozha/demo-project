package com.technokratos.agona.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "Модель для создания Task",
        title = "TaskRequest",
        example = """
                {
                    "title": "AGONA-1",
                    "description": "Реализовать функционал авторизации",
                    "starterCode": "public class Solution {\\\\n    public static void main(String[] args) {\\\\n        // code here\\\\n    }\\\\n}",
                    "deadline": "2024-12-31T23:59:59",
                    "reviewDeadline": "2025-01-07T23:59:59",
                    "maxScore": 100
                }
                """
)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequestDto {

    @Schema(
            description = "Уникальное название/код задания",
            type = "string",
            example = "AGONA-1",
            required = true,
            minLength = 3,
            maxLength = 50,
            pattern = "^[A-Z]+-[0-9]+$"
    )
    private String title;

    @Schema(
            description = "Подробное описание задания",
            type = "string",
            example = "Реализовать функционал авторизации с использованием JWT токенов",
            required = true,
            minLength = 10,
            maxLength = 2000
    )
    private String description;

    @Schema(
            description = "Начальный код для выполнения задания (шаблон)",
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
            maximum = "100",
            defaultValue = "100"
    )
    private Integer maxScore;

}
