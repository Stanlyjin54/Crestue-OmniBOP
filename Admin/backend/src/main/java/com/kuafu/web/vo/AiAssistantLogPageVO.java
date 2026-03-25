package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AiAssistantLogPageVO - rebuilt VO
 */
@Data
public class AiAssistantLogPageVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public AiAssistantLogPageVO() {}
    
    public AiAssistantLogPageVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}