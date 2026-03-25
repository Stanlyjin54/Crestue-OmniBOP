package com.kuafu.web.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TransferInfoVO - rebuilt VO
 */
@Data
public class TransferInfoVO {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    public TransferInfoVO() {}
    
    public TransferInfoVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}