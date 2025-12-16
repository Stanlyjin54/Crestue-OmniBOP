package com.kuafu.api.gateway.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * API路由实体类
 */
@Data
public class ApiRoute {
    /**
     * 路由ID
     */
    private String routeId;
    
    /**
     * 路由路径
     */
    private String path;
    
    /**
     * 目标URL
     */
    private String targetUrl;
    
    /**
     * 请求方法
     */
    private String method;
    
    /**
     * 认证方式
     */
    private String authType;
    
    /**
     * 是否启用
     */
    private boolean enabled;
    
    /**
     * 限流配置
     */
    private RateLimitConfig rateLimit;
    
    /**
     * 请求头映射
     */
    private Map<String, String> headerMappings;
    
    /**
     * 参数映射
     */
    private Map<String, String> paramMappings;
    
    /**
     * 过滤器列表
     */
    private List<String> filters;
    
    /**
     * 负载均衡策略
     */
    private String loadBalanceStrategy;
    
    /**
     * 超时时间（毫秒）
     */
    private int timeout;
    
    /**
     * 创建时间
     */
    private long createTime;
    
    /**
     * 更新时间
     */
    private long updateTime;
    
    /**
     * 限流配置类
     */
    @Data
    public static class RateLimitConfig {
        /**
         * 是否启用限流
         */
        private boolean enabled;
        
        /**
         * 限流类型：ip、user、api
         */
        private String type;
        
        /**
         * 每秒允许的请求数
         */
        private int requestsPerSecond;
        
        /**
         * 突发请求数
         */
        private int burstRequests;
        
        /**
         * 限流窗口大小（秒）
         */
        private int windowSize;
    }
}
