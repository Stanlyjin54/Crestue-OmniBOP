package com.kuafu.web.controller;

/**
 * ProductStatusEnumStatisticsController - 产品状态枚举统计控制器
 */
public enum ProductStatusEnumStatisticsController {
    
    /**
     * 在售产品统计
     */
    ON_SALE_STATS("1", "在售产品统计"),
    
    /**
     * 下架产品统计
     */
    OFF_SALE_STATS("0", "下架产品统计");
    
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
    ProductStatusEnumStatisticsController(String code, String description) {
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