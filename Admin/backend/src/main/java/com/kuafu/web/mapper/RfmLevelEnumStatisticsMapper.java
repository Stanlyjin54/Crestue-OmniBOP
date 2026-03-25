package com.kuafu.web.mapper;

/**
 * RfmLevelEnumStatisticsMapper - rebuilt enum for RFM level statistics
 */
public enum RfmLevelEnumStatisticsMapper {
    
    HIGH("1", "High Value"),
    LOW("0", "Low Value");
    
    private String code;
    private String description;
    
    RfmLevelEnumStatisticsMapper(String code, String description) {
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