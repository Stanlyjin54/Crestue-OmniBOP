package com.kuafu.web.service.impl;

/**
 * TransferStatusEnumServiceImpl - 转移状态枚举实现
 */
public enum TransferStatusEnumServiceImpl {
    
    /**
     * 已转移
     */
    TRANSFERRED("1", "已转移"),
    
    /**
     * 未转移
     */
    NOT_TRANSFERRED("0", "未转移");
    
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
    TransferStatusEnumServiceImpl(String code, String description) {
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