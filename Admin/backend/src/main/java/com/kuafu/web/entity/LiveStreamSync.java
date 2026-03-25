package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * LiveStreamSync - rebuilt entity
 */
@Data
public class LiveStreamSync {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public LiveStreamSync() {}
    
    public LiveStreamSync(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}