package com.kuafu.web.entity;

/**
 * PurchaseStatusEnumPurchaseStatusEnumIdStatusStatistic4879421dCount - 采购状态枚举采购状态枚举ID状态统计
 */
public enum PurchaseStatusEnumPurchaseStatusEnumIdStatusStatistic4879421dCount {
    
    /**
     * 有效状态统计
     */
    VALID_STATUS_STATS("1", "有效状态统计"),
    
    /**
     * 无效状态统计
     */
    INVALID_STATUS_STATS("0", "无效状态统计");
    
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
    PurchaseStatusEnumPurchaseStatusEnumIdStatusStatistic4879421dCount(String code, String description) {
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