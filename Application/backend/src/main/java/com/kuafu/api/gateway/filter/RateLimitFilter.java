package com.kuafu.api.gateway.filter;

import com.kuafu.api.gateway.domain.ApiContext;
import com.kuafu.api.gateway.service.RateLimitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 限流过滤器，用于实现API调用的限流功能
 */
@Component
@Slf4j
public class RateLimitFilter implements GatewayFilter {
    
    @Autowired
    private RateLimitService rateLimitService;
    
    @Override
    public boolean filter(ApiContext context) {
        log.info("执行限流过滤器: {}", context.getRequestId());
        
        // 检查路由是否启用限流
        if (context.getRoute() == null || context.getRoute().getRateLimit() == null || !context.getRoute().getRateLimit().isEnabled()) {
            log.info("路由未启用限流，直接通过: {}", context.getRoute().getPath());
            return true;
        }
        
        // 获取限流配置
        String limitType = context.getRoute().getRateLimit().getType();
        int requestsPerSecond = context.getRoute().getRateLimit().getRequestsPerSecond();
        
        // 生成限流键
        String limitKey = generateLimitKey(context, limitType);
        
        // 检查是否超过限流
        boolean isAllowed = rateLimitService.tryAcquire(limitKey, requestsPerSecond);
        
        if (!isAllowed) {
            log.error("API调用超过限流: {} {}", limitKey, requestsPerSecond);
            context.setErrorMessage("API调用频率超过限制");
            context.setResponseStatus(429);
            return false;
        }
        
        log.info("限流检查通过: {} {}", limitKey, requestsPerSecond);
        return true;
    }
    
    /**
     * 生成限流键
     * @param context API上下文
     * @param limitType 限流类型
     * @return 限流键
     */
    private String generateLimitKey(ApiContext context, String limitType) {
        String routeId = context.getRoute().getRouteId();
        
        switch (limitType) {
            case "ip":
                // 根据IP限流
                String ip = context.getRequestHeaders().get("X-Forwarded-For") != null ? 
                        context.getRequestHeaders().get("X-Forwarded-For") : 
                        "unknown";
                return "rate_limit:ip:" + ip + ":" + routeId;
            case "user":
                // 根据用户限流
                // TODO: 从认证信息中获取用户ID
                String userId = context.getAttribute("userId") != null ? 
                        context.getAttribute("userId").toString() : 
                        "anonymous";
                return "rate_limit:user:" + userId + ":" + routeId;
            case "api":
                // 根据API限流
                return "rate_limit:api:" + routeId;
            default:
                // 默认根据API限流
                return "rate_limit:api:" + routeId;
        }
    }
    
    @Override
    public int getOrder() {
        return 20; // 限流过滤器优先级高于监控，低于认证
    }
    
    @Override
    public String getName() {
        return "RateLimitFilter";
    }
}
