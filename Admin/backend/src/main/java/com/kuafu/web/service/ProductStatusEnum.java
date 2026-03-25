package com.kuafu.web.service;

/**
 * ProductStatusEnum - 产品状态枚举
 */
public enum ProductStatusEnum {
    
    ACTIVE("1", "在售"),
    INACTIVE("0", "下架");
    
    private final String code;
    private final String description;
    
    ProductStatusEnum(String code, String description) {
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