package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * RolePermission - rebuilt entity
 */
@Data
public class RolePermission {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public RolePermission() {}
    
    public RolePermission(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}