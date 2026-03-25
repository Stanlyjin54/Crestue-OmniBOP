package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * WarehouseInfoVO - rebuilt VO
 */
@Data
public class WarehouseInfoVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public WarehouseInfoVO() {}
    
    public WarehouseInfoVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}