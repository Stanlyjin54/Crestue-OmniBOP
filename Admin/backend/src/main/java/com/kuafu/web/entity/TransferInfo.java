package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * TransferInfo - rebuilt entity
 */
@Data
public class TransferInfo {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public TransferInfo() {}
    
    public TransferInfo(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}