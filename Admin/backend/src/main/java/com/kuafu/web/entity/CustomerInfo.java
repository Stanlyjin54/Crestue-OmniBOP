package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * CustomerInfo - rebuilt entity
 */
@Data
public class CustomerInfo {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public CustomerInfo() {}
    
    public CustomerInfo(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}