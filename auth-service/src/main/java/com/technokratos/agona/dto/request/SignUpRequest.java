package com.technokratos.agona.dto.request;

import com.technokratos.agona.validation.PasswordsMatch;
import com.technokratos.agona.validation.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на регистрацию нового пользователя")
@PasswordsMatch
public record SignUpRequest(

        @Schema(description = "Email пользователя", example = "user@example.com")
        @NotBlank(message = "Email не может быть пустым")
        @Email(message = "Невалидный формат email")
        String email,

        @Schema(description = "Отображаемое имя пользователя (никнейм)",
                example = "new_user123", minLength = 4, maxLength = 20)
        @NotBlank(message = "Nickname не может быть пустым")
        @Size(min = 4, max = 20, message = "Nickname должен быть от 4 до 20 символов")
        String nickname,

        @Schema(description = "Пароль (цифры, буквы, спецсимволы)",
                example = "P@ssw0rd!123", minLength = 8, maxLength = 24, format = "password")
        @ValidPassword
        String password,

        @Schema(description = "Повтор пароля",
                example = "P@ssw0rd!123", minLength = 8, maxLength = 24, format = "password")
        @NotBlank(message = "Подтверждение пароля не может быть пустым")
        String confirmPassword,

        @Schema(description = "Уникальный идентификатор устройства",
                example = "m0b1l3_d3v1c3_456789")
        @NotBlank(message = "Fingerprint не может быть пустым")
        @Size(min = 16, max = 64, message = "Fingerprint должен быть от 16 до 64 символов")
        String fingerprint
) {
}
