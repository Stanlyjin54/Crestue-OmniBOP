package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UserInfoVO - rebuilt VO
 */
@Data
public class UserInfoVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public UserInfoVO() {}
    
    public UserInfoVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}