package com.ya.tenpo.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class BucketConfig {

    @Value("${request.limit}")
    private long requestLimit;

    @Bean
    public Bucket bucket() {
        Bandwidth limit = Bandwidth.classic(requestLimit, Refill.greedy(requestLimit, Duration.ofMinutes(1)));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

}
