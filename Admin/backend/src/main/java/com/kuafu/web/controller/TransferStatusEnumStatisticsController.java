package com.kuafu.web.controller;

/**
 * TransferStatusEnumStatisticsController - 转账状态枚举统计控制器
 */
public enum TransferStatusEnumStatisticsController {
    
    /**
     * 转账成功统计
     */
    TRANSFER_SUCCESS_STATS("1", "转账成功统计"),
    
    /**
     * 转账失败统计
     */
    TRANSFER_FAILED_STATS("0", "转账失败统计");
    
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
    TransferStatusEnumStatisticsController(String code, String description) {
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