package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * OrderInfoAll - rebuilt entity
 */
@Data
public class OrderInfoAll {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public OrderInfoAll() {}
    
    public OrderInfoAll(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}