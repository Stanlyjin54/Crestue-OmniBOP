package com.kuafu.api.gateway.filter;

import com.kuafu.api.gateway.domain.ApiContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * API网关过滤器链
 */
@Component
public class GatewayFilterChain {
    
    @Autowired
    private List<GatewayFilter> filters;
    
    /**
     * 执行过滤器链
     * @param context API上下文
     * @return 是否执行成功
     */
    public boolean doFilter(ApiContext context) {
        // 按照优先级排序过滤器
        List<GatewayFilter> sortedFilters = filters.stream()
                .sorted((f1, f2) -> Integer.compare(f1.getOrder(), f2.getOrder()))
                .collect(Collectors.toList());
        
        // 执行每个过滤器
        for (GatewayFilter filter : sortedFilters) {
            try {
                boolean continueFilter = filter.filter(context);
                if (!continueFilter) {
                    // 如果过滤器返回false，中断过滤器链
                    return false;
                }
            } catch (Exception e) {
                // 处理过滤器执行异常
                context.setErrorMessage("过滤器执行失败: " + filter.getName());
                context.setResponseStatus(500);
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 获取过滤器列表
     * @return 过滤器列表
     */
    public List<GatewayFilter> getFilters() {
        return filters;
    }
}
