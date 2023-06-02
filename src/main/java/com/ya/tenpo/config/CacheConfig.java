package com.ya.tenpo.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Value("${cache.expire.time}")
    private Long cacheExpireTime;

    @Value("${cache.max.size}")
    private Long cacheMaxSize;

    @Bean
    public Cache<String, String> cache() {
        return Caffeine.newBuilder()
                .maximumSize(cacheMaxSize)
                .expireAfterWrite(cacheExpireTime, TimeUnit.MINUTES)
                .build();
    }

}
