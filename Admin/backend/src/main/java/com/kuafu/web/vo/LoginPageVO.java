package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * LoginPageVO - rebuilt VO
 */
@Data
public class LoginPageVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public LoginPageVO() {}
    
    public LoginPageVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}