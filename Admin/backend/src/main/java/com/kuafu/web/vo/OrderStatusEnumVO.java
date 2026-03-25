package com.kuafu.web.vo;

/**
 * OrderStatusEnumVO - 订单状态枚举视图对象
 */
public enum OrderStatusEnumVO {
    
    /**
     * 有效状态
     */
    VALID("1", "有效状态"),
    
    /**
     * 无效状态
     */
    INVALID("0", "无效状态");
    
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
    OrderStatusEnumVO(String code, String description) {
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