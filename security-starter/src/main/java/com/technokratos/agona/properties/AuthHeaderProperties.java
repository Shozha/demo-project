package com.technokratos.agona.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "security.auth.headers")
public class AuthHeaderProperties {

    private String userIdHeader = "X-User-Id";
    private String usernameHeader = "X-Username";
    private String rolesHeader = "X-User-Roles";
}
