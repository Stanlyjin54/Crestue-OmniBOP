package com.kuafu.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4jConfig - rebuilt config
 */
@Configuration
public class Knife4jConfig {
    
    private static final Logger log = LoggerFactory.getLogger(Knife4jConfig.class);
    
    /**
     * Initialize configuration
     */
    public void init() {
        log.info("Initializing Knife4jConfig");
        // Configuration initialization code
    }
}