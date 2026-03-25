package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * RolePageVO - rebuilt VO
 */
@Data
public class RolePageVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public RolePageVO() {}
    
    public RolePageVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}