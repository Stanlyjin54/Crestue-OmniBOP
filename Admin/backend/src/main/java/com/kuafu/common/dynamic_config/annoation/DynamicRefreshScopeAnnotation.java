package com.kuafu.common.dynamic_config.annoation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;


/**
 * DynamicRefreshScopeAnnotation - rebuilt config
 */
@Configuration
@Slf4j
public class DynamicRefreshScopeAnnotation {
    
    /**
     * Initialize configuration
     */
    public void init() {
        log.info("Initializing DynamicRefreshScopeAnnotation");
        // Configuration initialization code
    }
}