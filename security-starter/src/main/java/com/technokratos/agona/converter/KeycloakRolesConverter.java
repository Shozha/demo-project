package com.technokratos.agona.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class KeycloakRolesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String CLAIM_REALM_ACCESS = "realm_access";
    private static final String CLAIM_RESOURCE_ACCESS = "resource_access";
    private static final String CLAIM_ROLES = "roles";
    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        Map<String, Object> realmAccess = jwt.getClaimAsMap(CLAIM_REALM_ACCESS);
        if (realmAccess != null && realmAccess.containsKey(CLAIM_ROLES)) {
            List<String> realmRoles = (List<String>) realmAccess.get(CLAIM_ROLES);
            realmRoles.stream()
                    .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role.toUpperCase()))
                    .forEach(authorities::add);
        }

        Map<String, Object> resourceAccess = jwt.getClaimAsMap(CLAIM_RESOURCE_ACCESS);
        if (resourceAccess != null) {
            resourceAccess.forEach((clientId, clientClaims) -> {
                if (clientClaims instanceof Map<?, ?> claimsMap) {
                    Object roles = claimsMap.get(CLAIM_ROLES);
                    if (roles instanceof List<?> clientRoles) {
                        clientRoles.stream()
                                .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role.toString().toUpperCase()))
                                .forEach(authorities::add);
                    }
                }
            });
        }
        return authorities;
    }
}
