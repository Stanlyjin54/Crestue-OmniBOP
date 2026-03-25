package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UserRolePageVO - rebuilt VO
 */
@Data
public class UserRolePageVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public UserRolePageVO() {}
    
    public UserRolePageVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}