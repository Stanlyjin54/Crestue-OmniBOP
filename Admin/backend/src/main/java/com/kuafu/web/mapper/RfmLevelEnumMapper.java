package com.kuafu.web.mapper;

/**
 * RfmLevelEnumMapper - rebuilt enum for RFM level mapping
 */
public enum RfmLevelEnumMapper {
    
    HIGH("1", "High Value"),
    LOW("0", "Low Value");
    
    private String code;
    private String description;
    
    // 私有构造函数
    private RfmLevelEnumMapper(String code, String description) {
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