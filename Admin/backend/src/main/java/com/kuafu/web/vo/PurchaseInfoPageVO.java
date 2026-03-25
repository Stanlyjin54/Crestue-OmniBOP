package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * PurchaseInfoPageVO - rebuilt VO
 */
@Data
public class PurchaseInfoPageVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public PurchaseInfoPageVO() {}
    
    public PurchaseInfoPageVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}