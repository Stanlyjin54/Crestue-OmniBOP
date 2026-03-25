package com.kuafu.web.mapper;

/**
 * PurchaseStatusEnumStatisticsMapper - rebuilt enum for purchase status statistics
 */
public enum PurchaseStatusEnumStatisticsMapper {
    
    PENDING("1", "Pending"),
    COMPLETED("0", "Completed");
    
    private String code;
    private String description;
    
    PurchaseStatusEnumStatisticsMapper(String code, String description) {
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