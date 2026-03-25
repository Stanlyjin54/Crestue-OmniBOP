package com.kuafu.web.mapper;

/**
 * ProductStatusEnumMapper - rebuilt enum for product status mapping
 */
public enum ProductStatusEnumMapper {
    
    ACTIVE("1", "Active"),
    INACTIVE("0", "Inactive");
    
    private String code;
    private String description;
    
    ProductStatusEnumMapper(String code, String description) {
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