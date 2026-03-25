package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Login - rebuilt entity
 */
@Data
public class Login {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public Login() {}
    
    public Login(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}