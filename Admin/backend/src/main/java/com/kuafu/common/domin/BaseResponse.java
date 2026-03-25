package com.kuafu.common.domin;

import lombok.extern.slf4j.Slf4j;

/**
 * BaseResponse - rebuilt with proper logging
 */
@Slf4j
public class BaseResponse<T> {
    private int code;
    private String message;
    private T data;
    
    public BaseResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public T getData() {
        return data;
    }
}