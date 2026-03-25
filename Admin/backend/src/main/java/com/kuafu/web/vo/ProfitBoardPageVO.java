package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ProfitBoardPageVO - rebuilt VO
 */
@Data
public class ProfitBoardPageVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public ProfitBoardPageVO() {}
    
    public ProfitBoardPageVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}