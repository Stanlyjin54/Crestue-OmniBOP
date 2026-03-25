package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * GeneralOrdersPageVO - rebuilt VO
 */
@Data
public class GeneralOrdersPageVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public GeneralOrdersPageVO() {}
    
    public GeneralOrdersPageVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}