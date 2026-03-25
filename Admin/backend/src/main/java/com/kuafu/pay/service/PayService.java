package com.kuafu.pay.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * PayService - 支付服务接口
 */
public interface PayService {
    
    /**
     * 查找所有记录
     * 
     * @return 记录列表
     */
    List<Map<String, Object>> findAll();
    
    /**
     * 根据ID查找记录
     * 
     * @param id 记录ID
     * @return 记录详情
     */
    Map<String, Object> findById(Long id);
    
    /**
     * 保存记录
     * 
     * @param record 记录信息
     * @return 保存结果
     */
    int save(Map<String, Object> record);
    
    /**
     * 申请退款
     * 
     * @param refundOrderNo 退款订单号
     * @param paymentOrderId 支付订单ID
     * @param refundAmount 退款金额
     * @param reason 退款原因
     * @param totalAmount 总金额
     * @return 退款结果
     */
    boolean applyRefund(String refundOrderNo, String paymentOrderId, BigDecimal refundAmount, String reason, BigDecimal totalAmount);
}