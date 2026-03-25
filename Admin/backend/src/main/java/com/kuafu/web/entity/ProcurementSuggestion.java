package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * ProcurementSuggestion - rebuilt entity
 */
@Data
public class ProcurementSuggestion {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public ProcurementSuggestion() {}
    
    public ProcurementSuggestion(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}