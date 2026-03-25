package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AdRecommendInfoVO - rebuilt VO
 */
@Data
public class AdRecommendInfoVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public AdRecommendInfoVO() {}
    
    public AdRecommendInfoVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}