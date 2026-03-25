package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DynamicApiSettingPageVO - rebuilt VO
 */
@Data
public class DynamicApiSettingPageVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public DynamicApiSettingPageVO() {}
    
    public DynamicApiSettingPageVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}