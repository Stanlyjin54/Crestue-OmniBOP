package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * RolePermissionVO - rebuilt VO
 */
@Data
public class RolePermissionVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public RolePermissionVO() {}
    
    public RolePermissionVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}