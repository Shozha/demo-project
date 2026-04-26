package com.technokratos.agona.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Стандартный формат ошибки")
public record ErrorResponse(

        @Schema(description = "Код ошибки", example = "USER_NOT_FOUND")
        String errorCode,

        @Schema(description = "Сообщение ошибки", example = "Пользователь не найден")
        String errorMessage
) {
}
