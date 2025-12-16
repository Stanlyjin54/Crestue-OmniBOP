package com.kuafuweb.web.vo;

import java.util.Date;

import com.kuafuweb.common.domin.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>延迟任务-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DelayedTasksPageVO extends PageRequest {

    @JsonProperty(value = "id")
    private Integer id;
    @JsonProperty(value = "taskType")
    private String taskType;
    @JsonProperty(value = "taskData")
    private String taskData;
    @JsonProperty(value = "status")
    private String status;
    @JsonProperty(value = "executeTime")
    private Date executeTime;
    @JsonProperty(value = "createTime")
    private Date createTime;
    @JsonProperty(value = "updateTime")
    private Date updateTime;

}





