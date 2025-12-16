package com.kuafu.api.gateway.filter;

import com.kuafu.api.gateway.domain.ApiContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 监控过滤器，用于实现API调用的监控功能
 */
@Component
@Slf4j
public class MonitorFilter implements GatewayFilter {
    
    @Override
    public boolean filter(ApiContext context) {
        // 请求开始时记录
        if (context.getResponseData() == null) {
            log.info("API请求开始: {} {} {}", context.getRequestId(), context.getRoute().getMethod(), context.getRoute().getPath());
            return true;
        }
        
        // 请求结束时记录
        context.setEndTime(System.currentTimeMillis());
        long elapsedTime = context.getElapsedTime();
        
        log.info("API请求结束: {} {} {} 状态码: {} 耗时: {}ms", 
                context.getRequestId(), 
                context.getRoute().getMethod(), 
                context.getRoute().getPath(), 
                context.getResponseStatus(), 
                elapsedTime);
        
        // TODO: 实现监控数据存储和分析
        // 这里可以将监控数据存储到数据库或消息队列，用于后续分析和展示
        
        return true;
    }
    
    @Override
    public int getOrder() {
        return 100; // 监控过滤器优先级较低，最后执行
    }
    
    @Override
    public String getName() {
        return "MonitorFilter";
    }
}
