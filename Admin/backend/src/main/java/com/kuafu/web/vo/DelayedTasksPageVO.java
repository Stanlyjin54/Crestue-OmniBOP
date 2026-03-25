package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DelayedTasksPageVO - rebuilt VO
 */
@Data
public class DelayedTasksPageVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public DelayedTasksPageVO() {}
    
    public DelayedTasksPageVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}