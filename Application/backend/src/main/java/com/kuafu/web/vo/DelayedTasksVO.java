package com.kuafu.web.vo;

import lombok.AllArgsConstructor;
import com.kuafu.common.entity.StaticResource;
import java.util.List;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kuafu.common.deserializer.MultiDateDeserializer;
import com.kuafu.common.entity.BaseEntity;
/**
 * <p>延迟任务</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DelayedTasksVO  extends BaseEntity {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("taskType")
    private String taskType;
    @JsonProperty("taskData")
    private String taskData;
    @JsonProperty("status")
    private String status;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("executeTime")
    private Date executeTime;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("createTime")
    private Date createTime;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("updateTime")
    private Date updateTime;




}
