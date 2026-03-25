package com.kuafu.common.util;

import com.kuafu.common.domin.BaseResponse;

/**
 * ResultUtils - utility class for creating API responses
 */
public class ResultUtils {
    
    /**
     * Create success response
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(200, "Success", data);
    }
    
    /**
     * Create success response with message
     */
    public static <T> BaseResponse<T> success(String message, T data) {
        return new BaseResponse<>(200, message, data);
    }
    
    /**
     * Create error response
     */
    public static <T> BaseResponse<T> error(String message) {
        return new BaseResponse<>(500, message, null);
    }
    
    /**
     * Create error response with code
     */
    public static <T> BaseResponse<T> error(int code, String message) {
        return new BaseResponse<>(code, message, null);
    }
}