package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * InventoryRecord - rebuilt entity
 */
@Data
public class InventoryRecord {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public InventoryRecord() {}
    
    public InventoryRecord(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}