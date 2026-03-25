package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * PermissionPageVO - rebuilt VO
 */
@Data
public class PermissionPageVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public PermissionPageVO() {}
    
    public PermissionPageVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}