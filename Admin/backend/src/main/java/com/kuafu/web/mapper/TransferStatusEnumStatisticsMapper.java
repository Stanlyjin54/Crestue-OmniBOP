package com.kuafu.web.mapper;

/**
 * TransferStatusEnumStatisticsMapper - rebuilt enum for transfer status statistics
 */
public enum TransferStatusEnumStatisticsMapper {
    
    PENDING("1", "Pending"),
    COMPLETED("0", "Completed");
    
    private String code;
    private String description;
    
    TransferStatusEnumStatisticsMapper(String code, String description) {
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