package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AdRecommendInfoPageVO - rebuilt VO
 */
@Data
public class AdRecommendInfoPageVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public AdRecommendInfoPageVO() {}
    
    public AdRecommendInfoPageVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}