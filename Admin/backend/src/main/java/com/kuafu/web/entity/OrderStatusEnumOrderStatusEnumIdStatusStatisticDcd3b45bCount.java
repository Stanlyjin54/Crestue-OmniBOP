package com.kuafu.web.entity;

/**
 * OrderStatusEnumOrderStatusEnumIdStatusStatisticDcd3b45bCount - 订单状态枚举订单状态枚举ID状态统计
 */
public enum OrderStatusEnumOrderStatusEnumIdStatusStatisticDcd3b45bCount {
    
    /**
     * 待处理
     */
    PENDING("pending", "待处理"),
    
    /**
     * 处理中
     */
    PROCESSING("processing", "处理中"),
    
    /**
     * 已完成
     */
    COMPLETED("completed", "已完成"),
    
    /**
     * 已取消
     */
    CANCELLED("cancelled", "已取消");
    
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
    OrderStatusEnumOrderStatusEnumIdStatusStatisticDcd3b45bCount(String code, String description) {
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