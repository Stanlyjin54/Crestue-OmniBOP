package com.kuafu.web.vo;

/**
 * CustomerTagEnumPageVO - 客户标签枚举分页视图对象
 */
public enum CustomerTagEnumPageVO {
    
    /**
     * 重要标签
     */
    IMPORTANT("1", "重要标签"),
    
    /**
     * 普通标签
     */
    NORMAL("0", "普通标签");
    
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
    CustomerTagEnumPageVO(String code, String description) {
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