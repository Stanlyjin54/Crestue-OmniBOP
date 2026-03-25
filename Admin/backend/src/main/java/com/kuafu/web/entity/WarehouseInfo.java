package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * WarehouseInfo - rebuilt entity
 */
@Data
public class WarehouseInfo {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public WarehouseInfo() {}
    
    public WarehouseInfo(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}