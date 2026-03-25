package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CustomerInfoVO - rebuilt VO
 */
@Data
public class CustomerInfoVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public CustomerInfoVO() {}
    
    public CustomerInfoVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}