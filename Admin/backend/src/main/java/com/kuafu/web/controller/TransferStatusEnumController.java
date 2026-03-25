package com.kuafu.web.controller;

/**
 * TransferStatusEnumController - 转账状态枚举控制器
 */
public enum TransferStatusEnumController {
    
    /**
     * 转账成功
     */
    TRANSFER_SUCCESS("1", "转账成功"),
    
    /**
     * 转账失败
     */
    TRANSFER_FAILED("0", "转账失败");
    
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
    TransferStatusEnumController(String code, String description) {
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