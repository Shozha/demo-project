package ru.itis.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.itis.dto.response.client.UserRestrictionResponse;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserRestrictionsClient {

    private final RestTemplate restTemplate;

    @Value("${user-restrictions.url}")
    private String baseUrl;

    @Cacheable(value = "restrictions", key = "#userId")
    public UserRestrictionResponse getRestrictions(UUID userId) {
        String url = String.format("%s/%s", baseUrl, userId);
        return restTemplate.getForObject(url, UserRestrictionResponse.class);
    }

}
