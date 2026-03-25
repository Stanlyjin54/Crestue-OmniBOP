package com.kuafu.web.event;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * LoginTableVo - rebuilt VO
 */
@Data
public class LoginTableVo {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public LoginTableVo() {}
    
    public LoginTableVo(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}