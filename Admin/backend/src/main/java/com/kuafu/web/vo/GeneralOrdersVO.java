package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * GeneralOrdersVO - rebuilt VO
 */
@Data
public class GeneralOrdersVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public GeneralOrdersVO() {}
    
    public GeneralOrdersVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}