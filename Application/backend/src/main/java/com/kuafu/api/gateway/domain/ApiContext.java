package com.kuafu.api.gateway.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * API上下文，用于在过滤器链中传递请求和响应信息
 */
@Data
public class ApiContext {
    /**
     * 请求ID
     */
    private String requestId;
    
    /**
     * API路由信息
     */
    private ApiRoute route;
    
    /**
     * 请求参数
     */
    private Map<String, Object> requestParams;
    
    /**
     * 请求头
     */
    private Map<String, String> requestHeaders;
    
    /**
     * 响应数据
     */
    private Object responseData;
    
    /**
     * 响应状态码
     */
    private int responseStatus;
    
    /**
     * 响应头
     */
    private Map<String, String> responseHeaders;
    
    /**
     * 上下文属性，用于在过滤器之间传递数据
     */
    private Map<String, Object> attributes;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 请求开始时间
     */
    private long startTime;
    
    /**
     * 请求结束时间
     */
    private long endTime;
    
    public ApiContext() {
        this.requestId = generateRequestId();
        this.requestParams = new HashMap<>();
        this.requestHeaders = new HashMap<>();
        this.responseHeaders = new HashMap<>();
        this.attributes = new HashMap<>();
        this.startTime = System.currentTimeMillis();
    }
    
    /**
     * 生成请求ID
     * @return 请求ID
     */
    private String generateRequestId() {
        return "req_" + System.currentTimeMillis() + "_" + (int) (Math.random() * 10000);
    }
    
    /**
     * 获取上下文属性
     * @param key 属性键
     * @return 属性值
     */
    public <T> T getAttribute(String key) {
        return (T) attributes.get(key);
    }
    
    /**
     * 设置上下文属性
     * @param key 属性键
     * @param value 属性值
     */
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }
    
    /**
     * 获取请求处理耗时
     * @return 耗时（毫秒）
     */
    public long getElapsedTime() {
        return endTime - startTime;
    }
}
