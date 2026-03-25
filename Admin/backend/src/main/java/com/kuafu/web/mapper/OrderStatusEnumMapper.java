package com.kuafu.web.mapper;

/**
 * OrderStatusEnumMapper - rebuilt enum for order status mapping
 */
public enum OrderStatusEnumMapper {
    
    PENDING("1", "Pending"),
    COMPLETED("0", "Completed");
    
    private String code;
    private String description;
    
    OrderStatusEnumMapper(String code, String description) {
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