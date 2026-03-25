package com.kuafu.web.entity;

/**
 * CustomerTagEnumCustomerTagEnumIdStatusStatistic027cbb5fCount - 客户标签枚举统计
 */
public enum CustomerTagEnumCustomerTagEnumIdStatusStatistic027cbb5fCount {
    
    /**
     * 活跃客户标签统计
     */
    ACTIVE_CUSTOMER_TAG_STATS("1", "活跃客户标签统计"),
    
    /**
     * 非活跃客户标签统计
     */
    INACTIVE_CUSTOMER_TAG_STATS("0", "非活跃客户标签统计");
    
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
    CustomerTagEnumCustomerTagEnumIdStatusStatistic027cbb5fCount(String code, String description) {
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