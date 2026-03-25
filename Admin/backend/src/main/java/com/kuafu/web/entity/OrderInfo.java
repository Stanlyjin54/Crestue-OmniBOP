package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * OrderInfo - rebuilt entity
 */
@Data
public class OrderInfo {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public OrderInfo() {}
    
    public OrderInfo(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}