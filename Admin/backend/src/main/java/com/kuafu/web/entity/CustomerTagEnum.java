package com.kuafu.web.entity;

/**
 * CustomerTagEnum - 客户标签枚举
 */
public enum CustomerTagEnum {
    
    /**
     * 有效标签
     */
    VALID_TAG("1", "有效标签"),
    
    /**
     * 无效标签
     */
    INVALID_TAG("0", "无效标签");
    
    /**
     * 标签代码
     */
    private final String code;
    
    /**
     * 标签描述
     */
    private final String description;
    
    /**
     * 构造函数
     * 
     * @param code 标签代码
     * @param description 标签描述
     */
    CustomerTagEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    /**
     * 获取标签代码
     * 
     * @return 标签代码
     */
    public String getCode() {
        return code;
    }
    
    /**
     * 获取标签描述
     * 
     * @return 标签描述
     */
    public String getDescription() {
        return description;
    }
}