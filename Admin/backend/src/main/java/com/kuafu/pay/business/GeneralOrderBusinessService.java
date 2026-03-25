package com.kuafu.pay.business;

import com.kuafu.pay.db.domain.GeneralOrders;
import java.util.List;
import java.util.Map;

/**
 * GeneralOrderBusinessService - 通用订单业务服务接口
 */
public interface GeneralOrderBusinessService {
    
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
     * 订单退款
     * 
     * @param orderNo 订单号
     * @return 退款结果
     */
    Boolean refund(String orderNo);
    
    /**
     * 获取唯一订单号
     * 
     * @return 唯一订单号
     */
    String getUniqueOrderNo();
    
    /**
     * 获取订单信息
     * 
     * @param orderNo 订单号
     * @return 订单信息
     */
    GeneralOrders getOrder(String orderNo);
    
    /**
     * 创建支付订单
     * 
     * @return 创建结果
     */
    Object createPaymentOrder();
    
    /**
     * 更新支付订单ID到业务订单
     * 
     * @return 更新结果
     */
    Object updatePaymentOrdersIdToBusinessOrder();
    
    /**
     * 获取支付参数
     * 
     * @return 支付参数
     */
    Object getPaymentParam();
    
    /**
     * 支付成功回调
     * 
     * @return 回调结果
     */
    Object paySuccessCallback();
    
    /**
     * 订单过期
     * 
     * @return 过期结果
     */
    Object expireOrder();
    
    /**
     * 订单发货
     * 
     * @return 发货结果
     */
    Object deliverOrder();
    
    /**
     * 确认收货
     * 
     * @return 确认结果
     */
    Object confirmReceipt();
    
    /**
     * 解密
     * 
     * @return 解密结果
     */
    Object decryption();
    
    /**
     * 转换为帕斯卡命名
     * 
     * @return 转换结果
     */
    Object toPascalCase();
    
    /**
     * 取消支付
     * 
     * @return 取消结果
     */
    Object cancelPay();
}