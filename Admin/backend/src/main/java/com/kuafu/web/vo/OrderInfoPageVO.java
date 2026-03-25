package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * OrderInfoPageVO - rebuilt VO
 */
@Data
public class OrderInfoPageVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public OrderInfoPageVO() {}
    
    public OrderInfoPageVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}