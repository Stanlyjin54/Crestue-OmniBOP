package com.kuafuweb.pay.controller;

import com.google.common.collect.Lists;
import com.kuafuweb.common.annotation.Log;
import com.kuafuweb.common.domin.BaseResponse;
import com.kuafuweb.common.domin.ResultUtils;
import com.kuafuweb.pay.business.GeneralOrderBusinessService;
import com.kuafuweb.pay.business.OrderFacadeService;
import com.kuafuweb.pay.config.WxV3PayConfig;
import com.kuafuweb.pay.domain.OrderOperateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/generalOrder")
@Slf4j
public class GeneralOrderBusinessController {


    @Resource
    private OrderFacadeService orderFacadeService;



    /**
     * 订单处理的统一入口
     *
     * @param
     * @return
     */
    @PostMapping("/{operateName}")
    @Log
    public BaseResponse<?> handleOrder(@PathVariable String operateName, @RequestBody
    Map<String, Object> body) {

        return orderFacadeService.handleOrder(operateName, body);

    }

    /**
     * 获取支持的支付方式
     *
     * @return
     */



    @PostMapping("/callback/{payChannel}")
    @Log
    public Object paySuccessCallback(@PathVariable String payChannel, @RequestBody String requestData, @RequestHeader Map<String, String> headers) {
        log.info("paySuccessCallback payChannel:{},requestData:{},headers:{}", payChannel, requestData, headers);
        return orderFacadeService.handleOrderCallback(payChannel, requestData, headers);
    }
}





