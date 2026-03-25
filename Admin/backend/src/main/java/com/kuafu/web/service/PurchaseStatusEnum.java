package com.kuafu.web.service;

/**
 * PurchaseStatusEnum - 采购状态枚举
 */
public enum PurchaseStatusEnum {
    
    PENDING("1", "待采购"),
    COMPLETED("0", "已完成");
    
    private final String code;
    private final String description;
    
    PurchaseStatusEnum(String code, String description) {
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