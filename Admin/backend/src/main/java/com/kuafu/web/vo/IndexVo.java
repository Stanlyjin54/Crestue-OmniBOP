package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * IndexVo - rebuilt VO
 */
@Data
public class IndexVo {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public IndexVo() {}
    
    public IndexVo(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}