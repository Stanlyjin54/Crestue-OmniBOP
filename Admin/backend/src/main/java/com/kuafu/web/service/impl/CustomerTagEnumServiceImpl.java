package com.kuafu.web.service.impl;

/**
 * CustomerTagEnumServiceImpl - 客户标签枚举服务实现
 */
public enum CustomerTagEnumServiceImpl {
    
    /**
     * VIP客户
     */
    VIP_CUSTOMER("1", "VIP客户"),
    
    /**
     * 普通客户
     */
    REGULAR_CUSTOMER("0", "普通客户");
    
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
    CustomerTagEnumServiceImpl(String code, String description) {
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