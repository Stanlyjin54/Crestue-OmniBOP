package com.kuafu.pay.config;

import com.kuafu.common.dynamic_config.annoation.DBConfiguration;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 支付宝支付配置
 */
@Configuration
@DBConfiguration
@Data
public class AlipayConfig {
    
    /**
     * 是否启用支付宝支付
     */
    @Value("${alipay.enable:true}")
    private Boolean enable;
    
    /**
     * 支付宝应用ID
     */
    @Value("${alipay.app-id:xxxx}")
    private String appId;
    
    /**
     * 应用私钥
     */
    @Value("${alipay.private-key:xxxx}")
    private String privateKey;
    
    /**
     * 应用公钥
     */
    @Value("${alipay.public-key:xxxx}")
    private String publicKey;
    
    /**
     * 支付宝公钥
     */
    @Value("${alipay.alipay-public-key:xxxx}")
    private String alipayPublicKey;
    
    /**
     * 支付宝网关地址
     */
    @Value("${alipay.server-url:https://openapi.alipay.com/gateway.do}")
    private String serverUrl;
    
    /**
     * 返回格式
     */
    @Value("${alipay.format:json}")
    private String format;
    
    /**
     * 编码格式
     */
    @Value("${alipay.charset:utf-8}")
    private String charset;
    
    /**
     * 签名类型
     */
    @Value("${alipay.sign-type:RSA2}")
    private String signType;
    
    /**
     * 支付回调地址
     */
    @Value("${alipay.pay-back-url:http://localhost:6789/pay/callback/alipay}")
    private String payBackUrl;
    
    /**
     * 是否测试环境
     */
    @Value("${alipay.is-test:1}")
    private Boolean isTest;
    
    /**
     * 测试金额
     */
    @Value("${alipay.test-amount:0.01}")
    private String testAmount;
}