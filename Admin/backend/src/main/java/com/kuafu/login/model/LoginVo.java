package com.kuafu.login.model;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * LoginVo - rebuilt VO
 */
@Data
public class LoginVo {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public LoginVo() {}
    
    public LoginVo(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}