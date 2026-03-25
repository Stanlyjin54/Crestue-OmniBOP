package com.kuafu.web.mapper;

/**
 * ProductStatusEnumStatisticsMapper - rebuilt enum for product status statistics
 */
public enum ProductStatusEnumStatisticsMapper {
    
    ACTIVE("1", "Active"),
    INACTIVE("0", "Inactive");
    
    private String code;
    private String description;
    
    // 私有构造函数
    private ProductStatusEnumStatisticsMapper(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
}