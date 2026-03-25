package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * OrderItemPageVO - rebuilt VO
 */
@Data
public class OrderItemPageVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public OrderItemPageVO() {}
    
    public OrderItemPageVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}