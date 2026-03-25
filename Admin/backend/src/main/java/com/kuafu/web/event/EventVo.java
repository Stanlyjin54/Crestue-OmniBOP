package com.kuafu.web.event;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * EventVo - rebuilt VO
 */
@Data
public class EventVo {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public EventVo() {}
    
    public EventVo(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}