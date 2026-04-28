package com.technokratos.agona.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.starter")
@Getter
@Setter
public class SecurityStarterProperties {
    private String[] permitAll = {};
}
