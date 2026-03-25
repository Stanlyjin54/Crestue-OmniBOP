package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Role - rebuilt entity
 */
@Data
public class Role {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public Role() {}
    
    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}