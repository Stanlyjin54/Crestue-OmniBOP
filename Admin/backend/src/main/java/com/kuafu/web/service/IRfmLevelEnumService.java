package com.kuafu.web.service;

/**
 * IRfmLevelEnumService - RFM等级枚举服务
 */
public enum IRfmLevelEnumService {
    
    /**
     * 高价值客户等级
     */
    HIGH_VALUE_LEVEL("1", "高价值客户等级"),
    
    /**
     * 普通价值客户等级
     */
    NORMAL_VALUE_LEVEL("0", "普通价值客户等级");
    
    /**
     * 等级代码
     */
    private final String code;
    
    /**
     * 等级描述
     */
    private final String description;
    
    /**
     * 构造函数
     * 
     * @param code 等级代码
     * @param description 等级描述
     */
    IRfmLevelEnumService(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    /**
     * 获取等级代码
     * 
     * @return 等级代码
     */
    public String getCode() {
        return code;
    }
    
    /**
     * 获取等级描述
     * 
     * @return 等级描述
     */
    public String getDescription() {
        return description;
    }
}