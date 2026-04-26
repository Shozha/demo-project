package com.technokratos.agona.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на обновление токена (fingerprint из body)")
public record RefreshTokenRequest(

        @Schema(description = "Уникальный идентификатор устройства",
                example = "9f1d8b7a6c5d4e3f2a1b0c9d8e7f6a5b",
                minLength = 16, maxLength = 64)
        @NotBlank(message = "Fingerprint не может быть пустым")
        @Size(min = 16, max = 64, message = "Fingerprint должен быть от 16 до 64 символов")
        String fingerprint
) {
}
