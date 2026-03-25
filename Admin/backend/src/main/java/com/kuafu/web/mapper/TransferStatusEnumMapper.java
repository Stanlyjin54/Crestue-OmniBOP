package com.kuafu.web.mapper;

/**
 * TransferStatusEnumMapper - rebuilt enum for transfer status mapping
 */
public enum TransferStatusEnumMapper {
    
    PENDING("1", "Pending"),
    COMPLETED("0", "Completed");
    
    private String code;
    private String description;
    
    TransferStatusEnumMapper(String code, String description) {
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