package com.kuafu.web.controller;

/**
 * PurchaseStatusEnumStatisticsController - rebuilt enum
 */
public enum PurchaseStatusEnumStatisticsController {
    
    VALUE1("1", "Value 1"),
    VALUE2("0", "Value 2");
    
    private String code;
    private String description;
    
    private PurchaseStatusEnumStatisticsController(String code, String description) {
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