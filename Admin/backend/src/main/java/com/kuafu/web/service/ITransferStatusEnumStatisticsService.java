package com.kuafu.web.service;

/**
 * ITransferStatusEnumStatisticsService - 转移状态枚举统计服务
 */
public enum ITransferStatusEnumStatisticsService {
    
    /**
     * 已完成转移统计
     */
    COMPLETED_TRANSFER_STATS("1", "已完成转移统计"),
    
    /**
     * 未完成转移统计
     */
    INCOMPLETE_TRANSFER_STATS("0", "未完成转移统计");
    
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
    ITransferStatusEnumStatisticsService(String code, String description) {
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