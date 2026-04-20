package com.technokratos.agona.config;

import com.technokratos.agona.filter.HeaderBasedAuthenticationFilter;
import com.technokratos.agona.properties.AuthHeaderProperties;
import com.technokratos.agona.properties.CorsProperties;
import com.technokratos.agona.properties.SecurityConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@AutoConfiguration
@EnableConfigurationProperties({SecurityConfigProperties.class, CorsProperties.class, AuthHeaderProperties.class})
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityAutoConfig {

    private final SecurityConfigProperties configProps;
    private final CorsProperties corsProps;
    private final AuthHeaderProperties authHeadersProps;

    private static final String[] ACTUATOR_AND_SWAGGER_ENDPOINTS = {
            "/actuator/health", "/actuator/info", "/actuator/metrics",
            "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"
    };

    @Bean
    @ConditionalOnMissingBean
    public HeaderBasedAuthenticationFilter headerAuthFilter() {
        return new HeaderBasedAuthenticationFilter(authHeadersProps);
    }

    @Bean
    @Order(1)
    @ConditionalOnMissingBean(name = "actuatorSecurity")
    public SecurityFilterChain actuatorSecurity(HttpSecurity http) throws Exception {
        http.securityMatcher(ACTUATOR_AND_SWAGGER_ENDPOINTS)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    @Order(2)
    @ConditionalOnMissingBean(name = "securityFilterChain")
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   HeaderBasedAuthenticationFilter headerAuthFilter) throws Exception {
        http.securityMatcher("/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler()))
                .addFilterBefore(headerAuthFilter, UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(configProps.getPermitAll()).permitAll()
                .anyRequest().authenticated()
        );

        if (corsProps.isEnabled()) {
            http.cors(c -> c.configurationSource(corsConfigurationSource(corsProps)));
        }

        return http.build();
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, ex) -> {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("""
                    {"code": 401, "message": "Authentication required"}""");
        };
    }

    @Bean
    @ConditionalOnMissingBean(AccessDeniedHandler.class)
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("""
                    {"code": 403, "message": "Access denied"}""");
        };
    }

    @Bean
    @ConditionalOnMissingBean(CorsConfigurationSource.class)
    CorsConfigurationSource corsConfigurationSource(CorsProperties corsProperties) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(corsProperties.getAllowedOriginsPatterns());
        configuration.setAllowedMethods(corsProperties.getAllowedMethods());
        configuration.setAllowedHeaders(corsProperties.getAllowedHeaders());
        configuration.setExposedHeaders(corsProperties.getExposedHeaders());
        configuration.setAllowCredentials(corsProperties.isAllowCredentials());
        configuration.setMaxAge(corsProperties.getMaxAgeSeconds());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            throw new UsernameNotFoundException("No local users, JWT only");
        };
    }
}
