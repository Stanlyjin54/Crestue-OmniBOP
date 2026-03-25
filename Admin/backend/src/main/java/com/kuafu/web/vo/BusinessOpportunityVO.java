package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * BusinessOpportunityVO - rebuilt VO
 */
@Data
public class BusinessOpportunityVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public BusinessOpportunityVO() {}
    
    public BusinessOpportunityVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}