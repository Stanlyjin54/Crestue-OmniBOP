package com.kuafuweb.common.delay_task.test;


import com.kuafuweb.common.delay_task.domain.DelayTask;
import lombok.Data;

@Data
public class OrderDelayTask extends DelayTask {
    private String orderId;
}





