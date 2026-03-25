package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DynamicApiSetting - rebuilt entity
 */
@Data
public class DynamicApiSetting {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public DynamicApiSetting() {}
    
    public DynamicApiSetting(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}