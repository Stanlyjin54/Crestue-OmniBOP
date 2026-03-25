package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * AiAssistantLog - rebuilt entity
 */
@Data
public class AiAssistantLog {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public AiAssistantLog() {}
    
    public AiAssistantLog(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}