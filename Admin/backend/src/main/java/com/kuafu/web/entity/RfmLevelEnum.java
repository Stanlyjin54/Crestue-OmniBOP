package com.kuafu.web.entity;

/**
 * RfmLevelEnum - RFM等级枚举
 */
public enum RfmLevelEnum {
    
    /**
     * 有效等级
     */
    VALID_LEVEL("1", "有效等级"),
    
    /**
     * 无效等级
     */
    INVALID_LEVEL("0", "无效等级");
    
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
    RfmLevelEnum(String code, String description) {
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