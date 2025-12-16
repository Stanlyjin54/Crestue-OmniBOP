package com.kuafuweb.pay.db.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuafuweb.pay.db.domain.GeneralOrders;
import com.kuafuweb.pay.enums.PayStatus;

/**
 * @description 针对表【general_orders】的数据库操作Service
 * @createDate 2025-05-09 17:20:09
 */
public interface GeneralOrdersService extends IService<GeneralOrders> {

    /**
     * 根据订单号获取订单信息
     *
     * @param orderNo
     * @return
     */
    GeneralOrders getByOrderNo(String orderNo);


    /**
     * 根据支付订单号获取订单信息
     *
     * @param paymentOrderId
     * @return
     */
    GeneralOrders getByPaymentOrderId(String paymentOrderId);

    /**
     * 根据退款
     */

    GeneralOrders getByRefundId(String refundId);

    /**
     * 更新支付订单状态
     */

    void updatePaymentStatusByPaymentOrderId(String paymentOrderId, PayStatus originalStatus, PayStatus newStatus);

    /**
     * 更新支付订单状态
     *
     * @param orderNo
     * @param originalStatus
     * @param newStatus
     */

    void updatePaymentStatusByOrderNo(String orderNo, PayStatus originalStatus, PayStatus newStatus);


    GeneralOrders getByOpId(String opId);
}





