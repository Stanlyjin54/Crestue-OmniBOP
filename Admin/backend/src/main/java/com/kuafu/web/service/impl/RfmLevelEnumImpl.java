package com.kuafu.web.service.impl;

/**
 * RfmLevelEnumImpl - RFM等级枚举实现
 */
public enum RfmLevelEnumImpl {
    
    HIGH("1", "高价值"),
    MEDIUM("2", "中价值"),
    LOW("0", "低价值");
    
    private final String code;
    private final String description;
    
    RfmLevelEnumImpl(String code, String description) {
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