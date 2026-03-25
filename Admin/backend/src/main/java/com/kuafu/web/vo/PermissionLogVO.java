package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * PermissionLogVO - rebuilt VO
 */
@Data
public class PermissionLogVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public PermissionLogVO() {}
    
    public PermissionLogVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}