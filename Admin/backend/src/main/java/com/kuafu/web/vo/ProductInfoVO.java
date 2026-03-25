package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ProductInfoVO - rebuilt VO
 */
@Data
public class ProductInfoVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public ProductInfoVO() {}
    
    public ProductInfoVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}