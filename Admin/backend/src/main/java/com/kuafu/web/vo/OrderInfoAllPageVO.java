package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * OrderInfoAllPageVO - rebuilt VO
 */
@Data
public class OrderInfoAllPageVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public OrderInfoAllPageVO() {}
    
    public OrderInfoAllPageVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}