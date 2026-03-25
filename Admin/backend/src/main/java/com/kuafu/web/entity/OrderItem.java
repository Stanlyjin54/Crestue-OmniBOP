package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * OrderItem - rebuilt entity
 */
@Data
public class OrderItem {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public OrderItem() {}
    
    public OrderItem(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}