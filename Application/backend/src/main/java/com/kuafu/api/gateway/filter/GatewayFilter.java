package com.kuafu.api.gateway.filter;

import com.kuafu.api.gateway.domain.ApiContext;

/**
 * API网关过滤器接口
 */
public interface GatewayFilter {
    /**
     * 过滤方法
     * @param context API上下文
     * @return 是否继续执行后续过滤器
     */
    boolean filter(ApiContext context);
    
    /**
     * 获取过滤器优先级，值越小优先级越高
     * @return 优先级
     */
    int getOrder();
    
    /**
     * 获取过滤器名称
     * @return 过滤器名称
     */
    String getName();
}
