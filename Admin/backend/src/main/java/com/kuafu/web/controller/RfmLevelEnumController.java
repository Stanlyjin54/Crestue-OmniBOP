package com.kuafu.web.controller;

/**
 * RfmLevelEnumController - RFM等级枚举控制器
 */
public enum RfmLevelEnumController {
    
    /**
     * 高价值客户
     */
    HIGH_VALUE("1", "高价值客户"),
    
    /**
     * 普通客户
     */
    NORMAL_VALUE("0", "普通客户");
    
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
    RfmLevelEnumController(String code, String description) {
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