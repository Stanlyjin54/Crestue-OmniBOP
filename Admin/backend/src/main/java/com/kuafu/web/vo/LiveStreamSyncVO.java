package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * LiveStreamSyncVO - rebuilt VO
 */
@Data
public class LiveStreamSyncVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public LiveStreamSyncVO() {}
    
    public LiveStreamSyncVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}