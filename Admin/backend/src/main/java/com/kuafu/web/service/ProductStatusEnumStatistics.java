package com.kuafu.web.service;

/**
 * ProductStatusEnumStatistics - 产品状态统计枚举
 */
public enum ProductStatusEnumStatistics {
    
    IN_STOCK("1", "在库"),
    OUT_OF_STOCK("0", "缺货");
    
    private final String code;
    private final String description;
    
    ProductStatusEnumStatistics(String code, String description) {
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