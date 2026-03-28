package com.kuafu.web.controller;

import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.login.SecurityUtils;
import com.kuafu.web.entity.UserAddress;
import com.kuafu.web.service.UserAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * B2C 订单管理接口
 */
@RestController
@RequestMapping("/b2c/order")
@Api(tags = "B2C订单管理")
@RequiredArgsConstructor
public class B2COrderController {

    private final UserAddressService userAddressService;

    /**
     * B2C 创建订单
     * 实际调用 /api/data/invoke?table=order_info&method=save
     */
    @PostMapping("/create")
    @ApiOperation("B2C创建订单")
    public Object createOrder(@RequestBody Map<String, Object> request) {
        Integer userId = SecurityUtils.getUserId().intValue();

        // 添加用户ID到请求
        request.put("user_info_id", userId);
        request.put("order_date", new java.util.Date());
        request.put("status", "pending");

        // 返回给前端，前端通过 /api/data/invoke 创建订单
        return ResultUtils.success(Map.of(
            "userId", userId,
            "orderData", request,
            "message", "请使用 /api/data/invoke?table=order_info&method=save 创建订单"
        ));
    }

    /**
     * 获取订单列表
     * 实际调用 /api/data/invoke?table=order_info&method=list
     */
    @GetMapping("/list")
    @ApiOperation("获取B2C订单列表")
    public Object getOrderList(@RequestParam(required = false) String status) {
        Integer userId = SecurityUtils.getUserId().intValue();

        // 返回查询参数给前端
        return ResultUtils.success(Map.of(
            "userId", userId,
            "status", status,
            "message", "请使用 /api/data/invoke?table=order_info&method=list 查询订单",
            "params", Map.of(
                "table", "order_info",
                "method", "list",
                "user_info_id", userId,
                "status", status
            )
        ));
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/{orderId}")
    @ApiOperation("获取订单详情")
    public Object getOrderDetail(@PathVariable Integer orderId) {
        Integer userId = SecurityUtils.getUserId().intValue();

        return ResultUtils.success(Map.of(
            "userId", userId,
            "orderId", orderId,
            "message", "请使用 /api/data/invoke?table=order_info&method=get 查询订单详情",
            "params", Map.of(
                "table", "order_info",
                "method", "get",
                "order_info_id", orderId
            )
        ));
    }

    /**
     * 取消订单
     */
    @PutMapping("/{orderId}/cancel")
    @ApiOperation("取消订单")
    public Object cancelOrder(@PathVariable Integer orderId, @RequestParam(required = false) String reason) {
        Integer userId = SecurityUtils.getUserId().intValue();

        return ResultUtils.success(Map.of(
            "userId", userId,
            "orderId", orderId,
            "reason", reason,
            "message", "请使用 /api/data/invoke?table=order_info&method=update 更新订单状态",
            "params", Map.of(
                "table", "order_info",
                "method", "update",
                "order_info_id", orderId,
                "status", "cancelled",
                "cancel_reason", reason
            )
        ));
    }

    /**
     * 确认收货
     */
    @PutMapping("/{orderId}/confirm")
    @ApiOperation("确认收货")
    public Object confirmOrder(@PathVariable Integer orderId) {
        Integer userId = SecurityUtils.getUserId().intValue();

        return ResultUtils.success(Map.of(
            "userId", userId,
            "orderId", orderId,
            "message", "请使用 /api/data/invoke?table=order_info&method=update 更新订单状态",
            "params", Map.of(
                "table", "order_info",
                "method", "update",
                "order_info_id", orderId,
                "status", "completed"
            )
        ));
    }
}
