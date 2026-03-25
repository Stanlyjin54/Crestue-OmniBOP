package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DelayedTasks - rebuilt entity
 */
@Data
public class DelayedTasks {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public DelayedTasks() {}
    
    public DelayedTasks(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}