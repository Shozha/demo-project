package com.technokratos.agona.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Ошибки валидации входящих данных")
public record ValidationErrorResponse(

        @Schema(description = "HTTP-статус", example = "400")
        int httpStatus,

        @Schema(description = "Список нарушенных правил")
        List<String> errors
) {
}
