package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DelayedTasksVO - rebuilt VO
 */
@Data
public class DelayedTasksVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public DelayedTasksVO() {}
    
    public DelayedTasksVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}