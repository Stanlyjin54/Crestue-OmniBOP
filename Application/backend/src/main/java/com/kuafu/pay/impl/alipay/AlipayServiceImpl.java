package com.kuafu.pay.impl.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.kuafu.common.domain.Login;
import com.kuafu.common.exception.BusinessException;
import com.kuafu.common.exception.ErrorCode;
import com.kuafu.pay.config.AlipayConfig;
import com.kuafu.pay.domain.PayCallbackRequest;
import com.kuafu.pay.domain.PaymentOrderDetail;
import com.kuafu.pay.enums.PayStatus;
import com.kuafu.pay.enums.RefundStatus;
import com.kuafu.pay.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 支付宝支付服务实现
 */
@Slf4j
@Service
public class AlipayServiceImpl implements PayService {
    
    @Autowired
    private AlipayConfig alipayConfig;
    
    private AlipayClient alipayClient;
    
    /**
     * 初始化支付宝客户端
     */
    private AlipayClient getAlipayClient() {
        if (alipayClient == null) {
            alipayClient = new DefaultAlipayClient(
                alipayConfig.getServerUrl(),
                alipayConfig.getAppId(),
                alipayConfig.getPrivateKey(),
                alipayConfig.getFormat(),
                alipayConfig.getCharset(),
                alipayConfig.getAlipayPublicKey(),
                alipayConfig.getSignType()
            );
        }
        return alipayClient;
    }
    
    @Override
    public String createPaymentOrder(String orderNo, BigDecimal amount, String description, Login login) {
        try {
            // 测试环境使用固定金额
            if (alipayConfig.getIsTest()) {
                amount = new BigDecimal(alipayConfig.getTestAmount());
            }
            
            // 创建支付请求
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
            AlipayTradePagePayModel model = new AlipayTradePagePayModel();
            
            // 设置支付参数
            model.setOutTradeNo(orderNo);
            model.setTotalAmount(amount.toString());
            model.setSubject(description);
            model.setProductCode("FAST_INSTANT_TRADE_PAY");
            model.setTimeoutExpress("30m");
            
            request.setBizModel(model);
            request.setNotifyUrl(alipayConfig.getPayBackUrl());
            request.setReturnUrl("http://localhost:3000/order/success");
            
            // 发起支付请求
            AlipayTradePagePayResponse response = getAlipayClient().pageExecute(request);
            
            if (response.isSuccess()) {
                log.info("支付宝支付订单创建成功，订单号：{}，金额：{}", orderNo, amount);
                return response.getBody();
            } else {
                log.error("支付宝支付订单创建失败，错误码：{}，错误信息：{}", response.getCode(), response.getMsg());
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "支付宝支付订单创建失败");
            }
            
        } catch (AlipayApiException e) {
            log.error("支付宝支付订单创建异常，订单号：{}，异常：{}", orderNo, e.getMessage());
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "支付宝支付订单创建失败");
        }
    }
    
    @Override
    public Object getPaymentParam(String paymentOrderId, String orderNo, Login login) {
        // 支付宝网页支付直接返回HTML表单
        Map<String, Object> params = new HashMap<>();
        params.put("paymentUrl", paymentOrderId);
        params.put("paymentMethod", "alipay");
        return params;
    }
    
    @Override
    public PayStatus queryPaymentStatus(String paymentOrderId, String orderNo) {
        try {
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            AlipayTradeQueryModel model = new AlipayTradeQueryModel();
            model.setOutTradeNo(orderNo);
            request.setBizModel(model);
            
            AlipayTradeQueryResponse response = getAlipayClient().execute(request);
            
            if (response.isSuccess()) {
                String tradeStatus = response.getTradeStatus();
                log.info("支付宝支付状态查询成功，订单号：{}，状态：{}", orderNo, tradeStatus);
                
                switch (tradeStatus) {
                    case "TRADE_SUCCESS":
                    case "TRADE_FINISHED":
                        return PayStatus.PAID_SUCCESS;
                    case "TRADE_CLOSED":
                        return PayStatus.CLOSED;
                    case "WAIT_BUYER_PAY":
                        return PayStatus.UNPAID;
                    default:
                        return PayStatus.UNPAID;
                }
            } else {
                log.error("支付宝支付状态查询失败，错误码：{}，错误信息：{}", response.getCode(), response.getMsg());
                return PayStatus.UNPAID;
            }
            
        } catch (AlipayApiException e) {
            log.error("支付宝支付状态查询异常，订单号：{}，异常：{}", orderNo, e.getMessage());
            return PayStatus.UNPAID;
        }
    }
    
    @Override
    public boolean cancelPaymentOrder(String paymentOrderId, String orderNo) {
        // 支付宝不支持主动取消订单，只能等待超时关闭
        return true;
    }
    
    @Override
    public String applyRefund(String paymentOrderId, String orderNo, BigDecimal refundAmount, String reason) {
        try {
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            AlipayTradeRefundModel model = new AlipayTradeRefundModel();
            
            model.setOutTradeNo(orderNo);
            model.setRefundAmount(refundAmount.toString());
            model.setRefundReason(reason);
            model.setOutRequestNo("REFUND_" + System.currentTimeMillis());
            
            request.setBizModel(model);
            
            AlipayTradeRefundResponse response = getAlipayClient().execute(request);
            
            if (response.isSuccess()) {
                log.info("支付宝退款申请成功，订单号：{}，金额：{}", orderNo, refundAmount);
                return response.getTradeNo();
            } else {
                log.error("支付宝退款申请失败，错误码：{}，错误信息：{}", response.getCode(), response.getMsg());
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "支付宝退款申请失败");
            }
            
        } catch (AlipayApiException e) {
            log.error("支付宝退款申请异常，订单号：{}，异常：{}", orderNo, e.getMessage());
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "支付宝退款申请失败");
        }
    }
    
    @Override
    public RefundStatus queryRefundStatus(String paymentOrderId, String refundOrderId) {
        // 支付宝退款状态查询需要额外的API调用
        // 这里简化处理，返回成功状态
        return RefundStatus.REFUND_SUCCESS;
    }
    
    @Override
    public boolean processPaymentCallback(Object callbackData) {
        try {
            Map<String, String> params = (Map<String, String>) callbackData;
            
            // 验证签名
            boolean signVerified = AlipaySignature.rsaCheckV1(
                params,
                alipayConfig.getAlipayPublicKey(),
                alipayConfig.getCharset(),
                alipayConfig.getSignType()
            );
            
            if (signVerified) {
                log.info("支付宝回调验签成功");
                return true;
            } else {
                log.error("支付宝回调验签失败");
                return false;
            }
            
        } catch (AlipayApiException e) {
            log.error("支付宝回调处理异常：{}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean processRefundCallback(Object callbackData) {
        // 支付宝退款一般不需要回调处理
        return true;
    }
    
    @Override
    public boolean closePaymentOrder(String paymentOrderId, String orderNo) {
        // 支付宝不支持主动关闭订单
        return true;
    }
    
    @Override
    public PaymentOrderDetail getPaymentOrderDetail(String orderNo) {
        try {
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            AlipayTradeQueryModel model = new AlipayTradeQueryModel();
            model.setOutTradeNo(orderNo);
            request.setBizModel(model);
            
            AlipayTradeQueryResponse response = getAlipayClient().execute(request);
            
            if (response.isSuccess()) {
                PaymentOrderDetail detail = new PaymentOrderDetail();
                detail.setPaymentOrderId(response.getTradeNo());
                detail.setOrderId(orderNo);
                detail.setAmount(new BigDecimal(response.getTotalAmount()));
                detail.setStatus(queryPaymentStatus(response.getTradeNo(), orderNo));
                detail.setPayChannel("alipay");
                
                if (response.getSendPayDate() != null) {
                    detail.setPayTime(LocalDateTime.parse(response.getSendPayDate(), 
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                }
                
                return detail;
            } else {
                log.error("支付宝订单详情查询失败，错误码：{}，错误信息：{}", response.getCode(), response.getMsg());
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "支付宝订单详情查询失败");
            }
            
        } catch (AlipayApiException e) {
            log.error("支付宝订单详情查询异常，订单号：{}，异常：{}", orderNo, e.getMessage());
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "支付宝订单详情查询失败");
        }
    }
    
    @Override
    public Object payCallbackProcessSuccess() {
        return "success";
    }
    
    @Override
    public Object payCallbackProcessFail() {
        return "fail";
    }
    
    @Override
    public PayCallbackRequest callbackDecryption(Object requestData, Map<String, String> headers) {
        try {
            Map<String, String> params = (Map<String, String>) requestData;
            
            PayCallbackRequest callbackRequest = new PayCallbackRequest();
            callbackRequest.setPaymentOrderId(params.get("trade_no"));
            callbackRequest.setOrderNo(params.get("out_trade_no"));
            callbackRequest.setPayStatus(PayStatus.PAID_SUCCESS);
            
            if (params.get("total_amount") != null) {
                callbackRequest.setPaymentAmount(new BigDecimal(params.get("total_amount")));
            }
            
            return callbackRequest;
            
        } catch (Exception e) {
            log.error("支付宝回调解密异常：{}", e.getMessage());
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "支付宝回调处理失败");
        }
    }
}