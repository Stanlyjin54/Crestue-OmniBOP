package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ProcurementSuggestionVO - rebuilt VO
 */
@Data
public class ProcurementSuggestionVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public ProcurementSuggestionVO() {}
    
    public ProcurementSuggestionVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}