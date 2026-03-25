package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * RolePermissionPageVO - rebuilt VO
 */
@Data
public class RolePermissionPageVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public RolePermissionPageVO() {}
    
    public RolePermissionPageVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}