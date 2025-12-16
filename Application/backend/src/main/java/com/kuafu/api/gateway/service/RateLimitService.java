package com.kuafu.api.gateway.service;

/**
 * 限流服务，用于实现API调用的限流逻辑
 */
public interface RateLimitService {
    /**
     * 尝试获取令牌
     * @param key 限流键
     * @param limit 每秒允许的请求数
     * @return 是否获取成功
     */
    boolean tryAcquire(String key, int limit);
    
    /**
     * 重置限流计数器
     * @param key 限流键
     */
    void reset(String key);
    
    /**
     * 获取当前计数
     * @param key 限流键
     * @return 当前计数
     */
    long getCurrentCount(String key);
}
