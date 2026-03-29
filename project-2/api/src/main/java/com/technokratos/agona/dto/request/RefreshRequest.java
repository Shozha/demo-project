package com.technokratos.agona.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class RefreshRequest {

    @NotBlank
    private String refreshToken;
}
