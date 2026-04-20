package com.technokratos.agona.filter;

import com.technokratos.agona.dto.UserInfo;
import com.technokratos.agona.properties.AuthHeaderProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class HeaderBasedAuthenticationFilter extends OncePerRequestFilter {

    private final AuthHeaderProperties props;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<String> userId = Optional.ofNullable(request.getHeader(props.getUserIdHeader()));
            Optional<String> username = Optional.ofNullable(request.getHeader(props.getUsernameHeader()));
            Optional<String> roles = Optional.ofNullable(request.getHeader(props.getRolesHeader()));

            if (userId.isPresent() && !userId.get().isBlank()) {
                Authentication auth = createAuthentication(userId.get(), username.orElse(""), roles.orElse(""));
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(auth);
                SecurityContextHolder.setContext(context);
            }
            filterChain.doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private Authentication createAuthentication(String userId, String username, String rolesHeader) {
        List<GrantedAuthority> authorities = parseRoles(rolesHeader);

        UserInfo userInfo = UserInfo.builder()
                .userId(userId)
                .username(username)
                .authorities(authorities)
                .build();
        return new HeaderAuthenticationToken(userInfo, authorities);
    }

    private List<GrantedAuthority> parseRoles(String rolesHeader) {
        if (rolesHeader == null || rolesHeader.isBlank()) {
            return Collections.emptyList();
        }
        return Stream.of(rolesHeader.split(","))
                .map(String::trim)
                .filter(role -> !role.isEmpty())
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
