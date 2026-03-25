package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * SelectVO - rebuilt entity
 */
@Data
public class SelectVO {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public SelectVO() {}
    
    public SelectVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}