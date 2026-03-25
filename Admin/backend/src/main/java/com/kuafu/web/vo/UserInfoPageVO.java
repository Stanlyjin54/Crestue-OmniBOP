package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UserInfoPageVO - rebuilt VO
 */
@Data
public class UserInfoPageVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public UserInfoPageVO() {}
    
    public UserInfoPageVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}