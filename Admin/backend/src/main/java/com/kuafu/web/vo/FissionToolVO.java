package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * FissionToolVO - rebuilt VO
 */
@Data
public class FissionToolVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public FissionToolVO() {}
    
    public FissionToolVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}