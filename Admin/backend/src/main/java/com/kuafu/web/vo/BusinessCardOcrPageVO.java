package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * BusinessCardOcrPageVO - rebuilt VO
 */
@Data
public class BusinessCardOcrPageVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public BusinessCardOcrPageVO() {}
    
    public BusinessCardOcrPageVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}