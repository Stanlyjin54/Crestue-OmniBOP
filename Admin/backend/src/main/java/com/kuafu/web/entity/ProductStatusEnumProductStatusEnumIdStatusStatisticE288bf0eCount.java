package com.kuafu.web.entity;

/**
 * ProductStatusEnumProductStatusEnumIdStatusStatisticE288bf0eCount - 产品状态枚举产品状态枚举ID状态统计
 */
public enum ProductStatusEnumProductStatusEnumIdStatusStatisticE288bf0eCount {
    
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
    ProductStatusEnumProductStatusEnumIdStatusStatisticE288bf0eCount(String code, String description) {
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