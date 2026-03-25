package com.kuafu.web.controller;

/**
 * OrderStatusEnumStatisticsController - 订单状态枚举统计控制器
 */
public enum OrderStatusEnumStatisticsController {
    
    /**
     * 待支付订单统计
     */
    PENDING_PAYMENT_STATS("1", "待支付订单统计"),
    
    /**
     * 已支付订单统计
     */
    PAID_STATS("0", "已支付订单统计");
    
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
    OrderStatusEnumStatisticsController(String code, String description) {
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