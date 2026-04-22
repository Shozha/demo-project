package com.technokratos.agona.security.filter;

import com.technokratos.agona.security.provider.JwtAccessTokenProvider;
import com.technokratos.agona.security.userdetails.UserDetailsImpl;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtAccessTokenProvider accessTokenProvider;
    private static final String BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);

        if (StringUtils.hasText(token)) {
            try {
                if (accessTokenProvider.validateAccessToken(token)) {
                    String username = accessTokenProvider.getUsernameFromToken(token);
                    UUID userId = accessTokenProvider.getUserIdFromToken(token);
                    List<String> roles = accessTokenProvider.getRolesFromToken(token);

                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();

                    UserDetailsImpl principal = UserDetailsImpl.builder()
                            .id(userId)
                            .username(username)
                            .authorities(authorities)
                            .enabled(true)
                            .build();

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(principal, null, authorities);

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("JWT authenticated user '{}'", username);
                }
            } catch (JwtException ex) {
                log.warn("Failed to process JWT: {}", ex.getMessage());
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith(BEARER)) {
            return header.substring(7);
        }
        return null;
    }
}
