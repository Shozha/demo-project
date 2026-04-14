package com.technokratos.agona.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "refresh-token")
public class RefreshTokenProperties {

    private long expirationMs;
    private int maxTokensPerUser;
}
