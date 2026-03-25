package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * WarehouseInfoPageVO - rebuilt VO
 */
@Data
public class WarehouseInfoPageVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public WarehouseInfoPageVO() {}
    
    public WarehouseInfoPageVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}