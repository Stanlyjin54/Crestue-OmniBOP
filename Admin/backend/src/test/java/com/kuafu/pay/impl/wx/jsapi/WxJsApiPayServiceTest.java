package com.kuafu.pay.impl.wx.jsapi;

import com.kuafu.pay.factoary.PayFactory;
import com.kuafu.pay.service.PayService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import com.kuafu.common.config.TestRedisConfig;

import javax.annotation.Resource;

import java.math.BigDecimal;

@SpringBootTest(classes = com.kuafu.MisAppBackendApplication.class, 
               properties = {"spring.profiles.active=test", 
                            "spring.autoconfigure.exclude=org.redisson.spring.starter.RedissonAutoConfiguration,org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration"})
@ActiveProfiles("test")
@Import(TestRedisConfig.class)
class WxJsApiPayServiceTest {

    @Resource
    private PayFactory payFactory;

    @Test
    void applyRefund() throws InterruptedException {
        for (int i=0;i<10;i++){
            try {
                final PayService payService = payFactory.getPayService("wx_jsapi_mp");

                payService.applyRefund("refundOrderNo", "paymentOrderId", BigDecimal.valueOf(0.01), "reason", BigDecimal.valueOf(0.01));
            }catch (Exception e){
                e.printStackTrace();
            }
            Thread.sleep(5000);

        }

    }
}

