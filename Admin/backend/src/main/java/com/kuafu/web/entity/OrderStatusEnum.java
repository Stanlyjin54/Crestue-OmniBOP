package com.kuafu.web.entity;

/**
 * OrderStatusEnum - 订单状态枚举
 */
public enum OrderStatusEnum {
    
    /**
     * 已完成订单
     */
    COMPLETED_ORDER("1", "已完成订单"),
    
    /**
     * 未完成订单
     */
    INCOMPLETE_ORDER("0", "未完成订单");
    
    /**
     * 状态代码
     */
    private final String code;
    
    /**
     * 状态描述
     */
    private final String description;
    
    /**
     * 构造函数
     * 
     * @param code 状态代码
     * @param description 状态描述
     */
    OrderStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    /**
     * 获取状态代码
     * 
     * @return 状态代码
     */
    public String getCode() {
        return code;
    }
    
    /**
     * 获取状态描述
     * 
     * @return 状态描述
     */
    public String getDescription() {
        return description;
    }
}