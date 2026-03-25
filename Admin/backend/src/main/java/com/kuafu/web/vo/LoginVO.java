package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * LoginVO - rebuilt VO
 */
@Data
public class LoginVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public LoginVO() {}
    
    public LoginVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}