package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * OrderItemVO - rebuilt VO
 */
@Data
public class OrderItemVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public OrderItemVO() {}
    
    public OrderItemVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}