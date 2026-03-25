package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * PurchaseInfo - rebuilt entity
 */
@Data
public class PurchaseInfo {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public PurchaseInfo() {}
    
    public PurchaseInfo(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}