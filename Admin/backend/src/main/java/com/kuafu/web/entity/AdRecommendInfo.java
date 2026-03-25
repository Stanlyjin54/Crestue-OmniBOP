package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * AdRecommendInfo - rebuilt entity
 */
@Data
public class AdRecommendInfo {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public AdRecommendInfo() {}
    
    public AdRecommendInfo(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}