package com.kuafu.web.entity;

/**
 * ProductStatusEnum - 产品状态枚举
 */
public enum ProductStatusEnum {
    
    /**
     * 上架状态
     */
    ON_SHELF("1", "上架状态"),
    
    /**
     * 下架状态
     */
    OFF_SHELF("0", "下架状态");
    
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
    ProductStatusEnum(String code, String description) {
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