package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * BusinessOpportunity - rebuilt entity
 */
@Data
public class BusinessOpportunity {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public BusinessOpportunity() {}
    
    public BusinessOpportunity(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}