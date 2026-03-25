package com.kuafu.web.event;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * BaseLoginVo - rebuilt VO
 */
@Data
public class BaseLoginVo {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public BaseLoginVo() {}
    
    public BaseLoginVo(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}