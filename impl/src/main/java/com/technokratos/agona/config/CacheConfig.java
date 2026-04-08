package com.technokratos.agona.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@EnableCaching
@Configuration
public class CacheConfig {

    public static final String PRODUCTS_CACHE = "products";
    public static final String PRODUCT_CACHE  = "product";

    private static final long PRODUCTS_TTL_MINUTES = 5;
    private static final long PRODUCT_TTL_MINUTES  = 10;

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(PRODUCT_TTL_MINUTES))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withCacheConfiguration(PRODUCTS_CACHE, defaultConfig.entryTtl(Duration.ofMinutes(PRODUCTS_TTL_MINUTES)))
                .withCacheConfiguration(PRODUCT_CACHE, defaultConfig.entryTtl(Duration.ofMinutes(PRODUCT_TTL_MINUTES)))
                .build();
    }
}
