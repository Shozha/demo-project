package com.technokratos.agona.api;

import com.technokratos.agona.dto.request.RefreshTokenRequest;
import com.technokratos.agona.dto.response.ErrorResponse;
import com.technokratos.agona.dto.response.TokenPair;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Tokens", description = "Обновление токенов")
@RequestMapping(path = "/api/v1/tokens", produces = APPLICATION_JSON_VALUE)
public interface TokenApi {

    @Operation(summary = "Обновление пары токенов (token rotation)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Новая пара токенов",
                    content = @Content(schema = @Schema(implementation = TokenPair.class))),
            @ApiResponse(responseCode = "400", description = "Невалидный refresh-токен",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(path = "/refresh", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    TokenPair refresh(@CookieValue(name = "refresh_token", required = false) String refreshToken,
                      @RequestBody RefreshTokenRequest fingerprint);
}
