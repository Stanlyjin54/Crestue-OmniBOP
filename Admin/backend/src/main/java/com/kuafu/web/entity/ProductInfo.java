package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * ProductInfo - rebuilt entity
 */
@Data
public class ProductInfo {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public ProductInfo() {}
    
    public ProductInfo(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}