package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * GeneralOrders - rebuilt entity
 */
@Data
public class GeneralOrders {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public GeneralOrders() {}
    
    public GeneralOrders(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}