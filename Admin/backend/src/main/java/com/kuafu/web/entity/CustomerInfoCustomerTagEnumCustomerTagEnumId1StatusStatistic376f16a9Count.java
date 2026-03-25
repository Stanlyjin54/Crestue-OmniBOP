package com.kuafu.web.entity;

/**
 * CustomerInfoCustomerTagEnumCustomerTagEnumId1StatusStatistic376f16a9Count - 客户信息客户标签枚举统计
 */
public enum CustomerInfoCustomerTagEnumCustomerTagEnumId1StatusStatistic376f16a9Count {
    
    /**
     * VIP客户统计
     */
    VIP_CUSTOMER_STATS("1", "VIP客户统计"),
    
    /**
     * 普通客户统计
     */
    REGULAR_CUSTOMER_STATS("0", "普通客户统计");
    
    /**
     * 标签代码
     */
    private final String code;
    
    /**
     * 标签描述
     */
    private final String description;
    
    /**
     * 构造函数
     * 
     * @param code 标签代码
     * @param description 标签描述
     */
    CustomerInfoCustomerTagEnumCustomerTagEnumId1StatusStatistic376f16a9Count(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    /**
     * 获取标签代码
     * 
     * @return 标签代码
     */
    public String getCode() {
        return code;
    }
    
    /**
     * 获取标签描述
     * 
     * @return 标签描述
     */
    public String getDescription() {
        return description;
    }
}