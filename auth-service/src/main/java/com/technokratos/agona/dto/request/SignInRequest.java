package com.technokratos.agona.dto.request;

import com.technokratos.agona.validation.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на аутентификацию пользователя")
public record SignInRequest(

        @Schema(description = "Email пользователя", example = "john@example.com")
        @NotBlank(message = "Email не может быть пустым")
        @Email(message = "Невалидный формат email")
        String email,

        @Schema(description = "Пароль", example = "Str0ngP@ss!",
                minLength = 8, maxLength = 24, format = "password")
        @ValidPassword
        String password,

        @Schema(description = "Уникальный идентификатор устройства",
                example = "d3v1c3_f1ng3rpr1nt_123")
        @NotBlank(message = "Fingerprint не может быть пустым")
        @Size(min = 16, max = 64, message = "Fingerprint должен быть от 16 до 64 символов")
        String fingerprint
) {
}
