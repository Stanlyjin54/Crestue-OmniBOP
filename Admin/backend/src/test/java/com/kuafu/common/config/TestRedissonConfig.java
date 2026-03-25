package com.kuafu.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Test configuration to exclude Redisson
 */
@Configuration
@Profile("test")
public class TestRedissonConfig {
    
    /**
     * Exclude Redisson for tests
     */
    public TestRedissonConfig() {
        // This configuration will be active only during tests
    }
}