package com.kuafu.web.vo;

/**
 * ProductStatusEnumPageVO - rebuilt enum
 */
public enum ProductStatusEnumPageVO {
    
    VALUE1("1", "Value 1"),
    VALUE2("0", "Value 2");
    
    private String code;
    private String description;
    
    // Constructor
    ProductStatusEnumPageVO(String code, String description) {
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