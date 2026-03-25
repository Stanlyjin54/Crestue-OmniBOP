package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * UserRole - rebuilt entity
 */
@Data
public class UserRole {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public UserRole() {}
    
    public UserRole(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}