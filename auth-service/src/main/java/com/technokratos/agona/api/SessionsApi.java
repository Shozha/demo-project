package com.technokratos.agona.api;

import com.technokratos.agona.dto.request.RefreshTokenRequest;
import com.technokratos.agona.dto.request.SignInRequest;
import com.technokratos.agona.dto.response.ErrorResponse;
import com.technokratos.agona.dto.response.TokenPair;
import com.technokratos.agona.dto.response.ValidationErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Sessions", description = "Управление сессиями (вход / выход)")
@RequestMapping(path = "/api/v1/sessions", produces = APPLICATION_JSON_VALUE)
public interface SessionsApi {

    @Operation(summary = "Вход в систему (аутентификация)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешная аутентификация",
                    content = @Content(schema = @Schema(implementation = TokenPair.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации",
                    content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    TokenPair signIn(@RequestBody SignInRequest body);

    @Operation(summary = "Выход из системы (инвалидация refresh-токена)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Сессия завершена"),
            @ApiResponse(responseCode = "400", description = "Невалидный refresh-токен",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void logout(@CookieValue(name = "refresh_token", required = false) String refreshToken,
                @RequestBody RefreshTokenRequest fingerprint);
}
