package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ProductInfoPageVO - rebuilt VO
 */
@Data
public class ProductInfoPageVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public ProductInfoPageVO() {}
    
    public ProductInfoPageVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}