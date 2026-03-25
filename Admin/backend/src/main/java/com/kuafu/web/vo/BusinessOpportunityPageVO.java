package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * BusinessOpportunityPageVO - rebuilt VO
 */
@Data
public class BusinessOpportunityPageVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public BusinessOpportunityPageVO() {}
    
    public BusinessOpportunityPageVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}