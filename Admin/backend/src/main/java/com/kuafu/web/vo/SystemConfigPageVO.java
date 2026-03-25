package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * SystemConfigPageVO - rebuilt VO
 */
@Data
public class SystemConfigPageVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public SystemConfigPageVO() {}
    
    public SystemConfigPageVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}