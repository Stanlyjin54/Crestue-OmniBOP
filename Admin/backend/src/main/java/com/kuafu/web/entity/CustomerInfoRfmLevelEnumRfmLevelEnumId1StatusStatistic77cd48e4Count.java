package com.kuafu.web.entity;

/**
 * CustomerInfoRfmLevelEnumRfmLevelEnumId1StatusStatistic77cd48e4Count - 客户信息RFM等级枚举统计
 */
public enum CustomerInfoRfmLevelEnumRfmLevelEnumId1StatusStatistic77cd48e4Count {
    
    /**
     * 高价值客户统计
     */
    HIGH_VALUE_CUSTOMER_STATS("1", "高价值客户统计"),
    
    /**
     * 普通价值客户统计
     */
    NORMAL_VALUE_CUSTOMER_STATS("0", "普通价值客户统计");
    
    /**
     * 统计代码
     */
    private final String code;
    
    /**
     * 统计描述
     */
    private final String description;
    
    /**
     * 构造函数
     * 
     * @param code 统计代码
     * @param description 统计描述
     */
    CustomerInfoRfmLevelEnumRfmLevelEnumId1StatusStatistic77cd48e4Count(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    /**
     * 获取统计代码
     * 
     * @return 统计代码
     */
    public String getCode() {
        return code;
    }
    
    /**
     * 获取统计描述
     * 
     * @return 统计描述
     */
    public String getDescription() {
        return description;
    }
}