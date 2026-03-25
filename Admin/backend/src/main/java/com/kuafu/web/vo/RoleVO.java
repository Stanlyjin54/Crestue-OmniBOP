package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * RoleVO - rebuilt VO
 */
@Data
public class RoleVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public RoleVO() {}
    
    public RoleVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}