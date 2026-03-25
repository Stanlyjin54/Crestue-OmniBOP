package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * BusinessCardOcr - rebuilt entity
 */
@Data
public class BusinessCardOcr {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public BusinessCardOcr() {}
    
    public BusinessCardOcr(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}