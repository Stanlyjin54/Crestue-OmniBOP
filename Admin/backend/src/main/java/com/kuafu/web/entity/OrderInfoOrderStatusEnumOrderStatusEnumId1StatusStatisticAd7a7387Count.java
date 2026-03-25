package com.kuafu.web.entity;

/**
 * OrderInfoOrderStatusEnumOrderStatusEnumId1StatusStatisticAd7a7387Count - 订单信息订单状态枚举统计
 */
public enum OrderInfoOrderStatusEnumOrderStatusEnumId1StatusStatisticAd7a7387Count {
    
    /**
     * 已完成订单统计
     */
    COMPLETED_ORDER_STATS("1", "已完成订单统计"),
    
    /**
     * 未完成订单统计
     */
    INCOMPLETE_ORDER_STATS("0", "未完成订单统计");
    
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
    OrderInfoOrderStatusEnumOrderStatusEnumId1StatusStatisticAd7a7387Count(String code, String description) {
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