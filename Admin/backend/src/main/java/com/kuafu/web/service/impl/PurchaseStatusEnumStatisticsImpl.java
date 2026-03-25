package com.kuafu.web.service.impl;

/**
 * PurchaseStatusEnumStatisticsImpl - 采购状态统计枚举实现
 */
public enum PurchaseStatusEnumStatisticsImpl {
    
    PENDING("1", "待采购统计"),
    PROCESSING("2", "采购中统计"),
    COMPLETED("0", "已完成统计");
    
    private final String code;
    private final String description;
    
    PurchaseStatusEnumStatisticsImpl(String code, String description) {
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