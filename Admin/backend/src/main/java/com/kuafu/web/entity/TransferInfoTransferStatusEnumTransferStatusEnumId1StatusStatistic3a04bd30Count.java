package com.kuafu.web.entity;

/**
 * TransferInfoTransferStatusEnumTransferStatusEnumId1StatusStatistic3a04bd30Count - 转移信息转移状态枚举转移状态枚举ID1状态统计
 */
public enum TransferInfoTransferStatusEnumTransferStatusEnumId1StatusStatistic3a04bd30Count {
    
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
    TransferInfoTransferStatusEnumTransferStatusEnumId1StatusStatistic3a04bd30Count(String code, String description) {
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