package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * ProfitBoard - rebuilt entity
 */
@Data
public class ProfitBoard {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public ProfitBoard() {}
    
    public ProfitBoard(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}