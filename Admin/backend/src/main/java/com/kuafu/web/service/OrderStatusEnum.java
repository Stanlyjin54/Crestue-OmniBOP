package com.kuafu.web.service;

/**
 * OrderStatusEnum - 订单状态枚举
 */
public enum OrderStatusEnum {
    
    PENDING("1", "待处理"),
    COMPLETED("0", "已完成");
    
    private final String code;
    private final String description;
    
    OrderStatusEnum(String code, String description) {
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