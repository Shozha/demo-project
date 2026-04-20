package com.technokratos.agona.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "security.cors")
public class CorsProperties {

    private boolean enabled = false;
    private List<String> allowedOriginsPatterns = List.of();
    private List<String> allowedMethods = List.of("GET", "POST");
    private List<String> allowedHeaders = List.of("X-User-Id", "X-Username", "X-User-Roles", "Content-Type", "Authorization");
    private List<String> exposedHeaders = List.of();
    private boolean allowCredentials = false;
    private Long maxAgeSeconds = 3600L;
}
