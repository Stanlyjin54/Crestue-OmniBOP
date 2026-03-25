package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * FissionToolPageVO - rebuilt VO
 */
@Data
public class FissionToolPageVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public FissionToolPageVO() {}
    
    public FissionToolPageVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}