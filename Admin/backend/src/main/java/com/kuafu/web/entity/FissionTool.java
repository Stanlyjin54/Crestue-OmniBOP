package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * FissionTool - rebuilt entity
 */
@Data
public class FissionTool {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public FissionTool() {}
    
    public FissionTool(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}