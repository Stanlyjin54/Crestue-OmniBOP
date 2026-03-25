package com.kuafu.web.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * UserInfo - rebuilt entity
 */
@Data
public class UserInfo {
    
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public UserInfo() {}
    
    public UserInfo(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}