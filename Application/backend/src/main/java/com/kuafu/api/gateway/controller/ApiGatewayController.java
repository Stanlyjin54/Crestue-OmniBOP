package com.kuafu.api.gateway.controller;

import com.kuafu.api.gateway.domain.ApiContext;
import com.kuafu.api.gateway.domain.ApiRoute;
import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API网关管理REST API
 */
@RestController
@RequestMapping("/api/gateway")
@Slf4j
public class ApiGatewayController {
    
    // 模拟数据存储，实际项目中应使用数据库
    private final Map<String, ApiRoute> routes = new HashMap<>();
    
    public ApiGatewayController() {
        // 初始化一些示例路由
        initSampleRoutes();
    }
    
    /**
     * 初始化示例路由
     */
    private void initSampleRoutes() {
        ApiRoute route1 = new ApiRoute();
        route1.setRouteId("route_001");
        route1.setPath("/api/proxy/*");
        route1.setTargetUrl("http://localhost:8080");
        route1.setMethod("*");
        route1.setAuthType("none");
        route1.setEnabled(true);
        route1.setCreateTime(System.currentTimeMillis());
        route1.setUpdateTime(System.currentTimeMillis());
        routes.put(route1.getRouteId(), route1);
        
        ApiRoute route2 = new ApiRoute();
        route2.setRouteId("route_002");
        route2.setPath("/api/test");
        route2.setTargetUrl("http://localhost:8080/test");
        route2.setMethod("GET");
        route2.setAuthType("jwt");
        route2.setEnabled(true);
        route2.setCreateTime(System.currentTimeMillis());
        route2.setUpdateTime(System.currentTimeMillis());
        routes.put(route2.getRouteId(), route2);
    }
    
    /**
     * 获取所有路由列表
     */
    @GetMapping("/routes")
    public BaseResponse<List<ApiRoute>> getAllRoutes() {
        log.info("获取所有路由列表");
        return ResultUtils.success(new ArrayList<>(routes.values()));
    }
    
    /**
     * 根据ID获取路由详情
     */
    @GetMapping("/routes/{routeId}")
    public BaseResponse<ApiRoute> getRouteById(@PathVariable String routeId) {
        log.info("根据ID获取路由详情: {}", routeId);
        ApiRoute route = routes.get(routeId);
        if (route == null) {
            return ResultUtils.error(HttpStatus.NOT_FOUND.value(), "路由不存在");
        }
        return ResultUtils.success(route);
    }
    
    /**
     * 创建新路由
     */
    @PostMapping("/routes")
    public BaseResponse<ApiRoute> createRoute(@RequestBody ApiRoute route) {
        log.info("创建新路由: {}", route.getPath());
        
        // 生成路由ID
        String routeId = "route_" + System.currentTimeMillis();
        route.setRouteId(routeId);
        route.setCreateTime(System.currentTimeMillis());
        route.setUpdateTime(System.currentTimeMillis());
        
        routes.put(routeId, route);
        return ResultUtils.success(route);
    }
    
    /**
     * 更新路由
     */
    @PutMapping("/routes/{routeId}")
    public BaseResponse<ApiRoute> updateRoute(@PathVariable String routeId, @RequestBody ApiRoute route) {
        log.info("更新路由: {}", routeId);
        
        if (!routes.containsKey(routeId)) {
            return ResultUtils.error(HttpStatus.NOT_FOUND.value(), "路由不存在");
        }
        
        route.setRouteId(routeId);
        route.setUpdateTime(System.currentTimeMillis());
        routes.put(routeId, route);
        
        return ResultUtils.success(route);
    }
    
    /**
     * 删除路由
     */
    @DeleteMapping("/routes/{routeId}")
    public BaseResponse<Boolean> deleteRoute(@PathVariable String routeId) {
        log.info("删除路由: {}", routeId);
        
        if (!routes.containsKey(routeId)) {
            return ResultUtils.error(HttpStatus.NOT_FOUND.value(), "路由不存在");
        }
        
        routes.remove(routeId);
        return ResultUtils.success(true);
    }
    
    /**
     * 启用路由
     */
    @PostMapping("/routes/{routeId}/enable")
    public BaseResponse<Boolean> enableRoute(@PathVariable String routeId) {
        log.info("启用路由: {}", routeId);
        
        ApiRoute route = routes.get(routeId);
        if (route == null) {
            return ResultUtils.error(HttpStatus.NOT_FOUND.value(), "路由不存在");
        }
        
        route.setEnabled(true);
        route.setUpdateTime(System.currentTimeMillis());
        routes.put(routeId, route);
        
        return ResultUtils.success(true);
    }
    
    /**
     * 禁用路由
     */
    @PostMapping("/routes/{routeId}/disable")
    public BaseResponse<Boolean> disableRoute(@PathVariable String routeId) {
        log.info("禁用路由: {}", routeId);
        
        ApiRoute route = routes.get(routeId);
        if (route == null) {
            return ResultUtils.error(HttpStatus.NOT_FOUND.value(), "路由不存在");
        }
        
        route.setEnabled(false);
        route.setUpdateTime(System.currentTimeMillis());
        routes.put(routeId, route);
        
        return ResultUtils.success(true);
    }
    
    /**
     * 获取监控数据
     */
    @GetMapping("/monitor")
    public BaseResponse<Map<String, Object>> getMonitorData() {
        log.info("获取监控数据");
        
        // 模拟监控数据，实际项目中应从数据库或监控系统获取
        Map<String, Object> monitorData = new HashMap<>();
        monitorData.put("totalRequests", 1000);
        monitorData.put("successRequests", 950);
        monitorData.put("errorRequests", 50);
        monitorData.put("avgResponseTime", 150);
        monitorData.put("maxResponseTime", 1000);
        
        return ResultUtils.success(monitorData);
    }
    
    /**
     * 获取限流配置
     */
    @GetMapping("/rateLimit/{routeId}")
    public BaseResponse<ApiRoute.RateLimitConfig> getRateLimitConfig(@PathVariable String routeId) {
        log.info("获取限流配置: {}", routeId);
        
        ApiRoute route = routes.get(routeId);
        if (route == null) {
            return ResultUtils.error(HttpStatus.NOT_FOUND.value(), "路由不存在");
        }
        
        return ResultUtils.success(route.getRateLimit());
    }
    
    /**
     * 更新限流配置
     */
    @PutMapping("/rateLimit/{routeId}")
    public BaseResponse<ApiRoute.RateLimitConfig> updateRateLimitConfig(@PathVariable String routeId, @RequestBody ApiRoute.RateLimitConfig rateLimitConfig) {
        log.info("更新限流配置: {}", routeId);
        
        ApiRoute route = routes.get(routeId);
        if (route == null) {
            return ResultUtils.error(HttpStatus.NOT_FOUND.value(), "路由不存在");
        }
        
        route.setRateLimit(rateLimitConfig);
        route.setUpdateTime(System.currentTimeMillis());
        routes.put(routeId, route);
        
        return ResultUtils.success(route.getRateLimit());
    }
}
