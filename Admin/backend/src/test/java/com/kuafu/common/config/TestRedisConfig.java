package com.kuafu.common.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Test Redis configuration that uses a mock connection
 */
@TestConfiguration
@Profile("test")
public class TestRedisConfig {
    
    /**
     * Create a mock Redis connection factory for tests
     * This prevents Redisson from trying to connect to a real Redis server
     */
    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactory() {
        // Return a mock connection factory that won't actually connect to Redis
        return new LettuceConnectionFactory();
    }
    
    /**
     * Create a Redis template for tests
     */
    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }
}