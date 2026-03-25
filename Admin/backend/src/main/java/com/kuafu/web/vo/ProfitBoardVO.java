package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ProfitBoardVO - rebuilt VO
 */
@Data
public class ProfitBoardVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public ProfitBoardVO() {}
    
    public ProfitBoardVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}