package com.kuafu.web.mapper;

/**
 * CustomerTagEnumMapper - rebuilt enum
 */
public enum CustomerTagEnumMapper {
    
    VALUE1("1", "Value 1"),
    VALUE2("0", "Value 2");
    
    private String code;
    private String description;
    
    CustomerTagEnumMapper(String code, String description) {
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