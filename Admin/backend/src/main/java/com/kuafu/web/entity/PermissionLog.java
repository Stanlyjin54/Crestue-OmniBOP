package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * PermissionLog - rebuilt entity
 */
@Data
public class PermissionLog {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public PermissionLog() {}
    
    public PermissionLog(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}