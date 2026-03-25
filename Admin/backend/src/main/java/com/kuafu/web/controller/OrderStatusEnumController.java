package com.kuafu.web.controller;

/**
 * OrderStatusEnumController - 订单状态枚举控制器
 */
public enum OrderStatusEnumController {
    
    /**
     * 待支付
     */
    PENDING_PAYMENT("1", "待支付"),
    
    /**
     * 已支付
     */
    PAID("0", "已支付");
    
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
    OrderStatusEnumController(String code, String description) {
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