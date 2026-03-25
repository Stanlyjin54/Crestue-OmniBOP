package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * OrderInfoVO - rebuilt VO
 */
@Data
public class OrderInfoVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public OrderInfoVO() {}
    
    public OrderInfoVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}