package com.kuafu.web.vo;

/**
 * RfmLevelEnumVO - rebuilt enum
 */
public enum RfmLevelEnumVO {
    
    HIGH("high", "High Value"),
    MEDIUM("medium", "Medium Value"),
    LOW("low", "Low Value");
    
    private String code;
    private String description;
    
    // Constructor
    RfmLevelEnumVO(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
}