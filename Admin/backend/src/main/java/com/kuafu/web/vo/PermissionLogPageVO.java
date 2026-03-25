package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * PermissionLogPageVO - rebuilt VO
 */
@Data
public class PermissionLogPageVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public PermissionLogPageVO() {}
    
    public PermissionLogPageVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}