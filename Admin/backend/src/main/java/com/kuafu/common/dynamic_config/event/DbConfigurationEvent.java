package com.kuafu.common.dynamic_config.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * DbConfigurationEvent - rebuilt config
 */
@Configuration
@Slf4j
public class DbConfigurationEvent {
    
    /**
     * Initialize configuration
     */
    public void init() {
        log.info("Initializing DbConfigurationEvent");
        // Configuration initialization code
    }
}