package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DynamicApiSettingVO - rebuilt VO
 */
@Data
public class DynamicApiSettingVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public DynamicApiSettingVO() {}
    
    public DynamicApiSettingVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}