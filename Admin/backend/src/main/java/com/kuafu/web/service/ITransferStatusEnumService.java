package com.kuafu.web.service;

/**
 * ITransferStatusEnumService - rebuilt enum
 */
public enum ITransferStatusEnumService {
    
    VALUE1("1", "Value 1"),
    VALUE2("0", "Value 2");
    
    private String code;
    private String description;
    
    ITransferStatusEnumService(String code, String description) {
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