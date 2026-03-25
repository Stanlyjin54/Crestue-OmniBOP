package com.kuafu.web.service.impl;

/**
 * PurchaseStatusEnumImpl - 采购状态枚举实现
 */
public enum PurchaseStatusEnumImpl {
    
    PENDING("1", "待采购"),
    PROCESSING("2", "采购中"),
    COMPLETED("0", "已完成");
    
    private final String code;
    private final String description;
    
    PurchaseStatusEnumImpl(String code, String description) {
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