package com.kuafu.pay.factoary;

import com.kuafu.pay.impl.wx.jsapi.WxJsApiMpPayService;
import com.kuafu.pay.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * PayFactory - 支付服务工厂类
 */
@Component
@Slf4j
public class PayFactory {
    
    @Resource
    private WxJsApiMpPayService wxJsApiMpPayService;
    
    private final Map<String, PayService> payServiceMap = new HashMap<>();
    
    /**
     * 初始化支付服务映射
     */
    private void initPayServiceMap() {
        payServiceMap.put("wx_jsapi_mp", wxJsApiMpPayService);
    }
    
    /**
     * 根据支付类型获取支付服务
     * 
     * @param payType 支付类型
     * @return 支付服务实例
     */
    public PayService getPayService(String payType) {
        if (payServiceMap.isEmpty()) {
            initPayServiceMap();
        }
        
        PayService payService = payServiceMap.get(payType);
        if (payService == null) {
            log.error("未找到支付类型对应的支付服务: {}", payType);
            throw new IllegalArgumentException("不支持的支付类型: " + payType);
        }
        
        return payService;
    }
}