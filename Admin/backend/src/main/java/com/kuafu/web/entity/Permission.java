package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Permission - rebuilt entity
 */
@Data
public class Permission {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public Permission() {}
    
    public Permission(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}