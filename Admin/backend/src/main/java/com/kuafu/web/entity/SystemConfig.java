package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * SystemConfig - rebuilt entity
 */
@Data
public class SystemConfig {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public SystemConfig() {}
    
    public SystemConfig(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}