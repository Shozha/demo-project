package com.technokratos.agona.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Пара Access / Refresh токенов")
public record TokenPair(

        @Schema(description = "JWT Access-токен",
                example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0...")
        String accessToken,

        @Schema(description = "Refresh-токен",
                example = "d41d8cd98f00b204e9800998ecf8427e")
        String refreshToken
) {
}
