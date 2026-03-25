package com.kuafu.web.service.impl;

/**
 * RfmLevelEnumStatisticsImpl - RFM等级统计枚举实现
 */
public enum RfmLevelEnumStatisticsImpl {
    
    HIGH("1", "高价值统计"),
    MEDIUM("2", "中价值统计"),
    LOW("0", "低价值统计");
    
    private final String code;
    private final String description;
    
    RfmLevelEnumStatisticsImpl(String code, String description) {
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