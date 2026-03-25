package com.kuafu.web.service.impl;

/**
 * ProductStatusEnumStatisticsServiceImpl - 产品状态统计枚举实现
 */
public enum ProductStatusEnumStatisticsServiceImpl {
    
    /**
     * 上架状态
     */
    ONLINE("1", "上架"),
    
    /**
     * 下架状态
     */
    OFFLINE("0", "下架");
    
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
    ProductStatusEnumStatisticsServiceImpl(String code, String description) {
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