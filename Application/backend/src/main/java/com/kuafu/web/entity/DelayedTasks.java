package com.kuafu.web.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.*;
import com.kuafu.common.entity.StaticResource;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kuafu.web.annotation.IsNotNullField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import com.kuafu.common.annotation.*;
/**
 * <p>  延迟任务 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("delayed_tasks")
public class DelayedTasks  {
    @TableId(value = "id", type = IdType.AUTO)
    @JsonProperty("id")
    
    
    
    private Integer id;
    @JsonProperty("task_type")
    @IsNotNullField(description = "任务类型")
    
    
    @TableField("task_type")
    private String taskType;
    @JsonProperty("task_data")
    @IsNotNullField(description = "任务数据")
    
    
    @TableField("task_data")
    private String taskData;
    @JsonProperty("status")
    @IsNotNullField(description = "任务状态")
    
    
    @TableField("status")
    private String status;
    @JsonProperty("execute_time")
    @IsNotNullField(description = "执行时间")
    
    
    @TableField("execute_time")
    private Date executeTime;
    @JsonProperty("create_time")
    @IsNotNullField(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    
    
    private Date createTime;
    @JsonProperty("update_time")
    @IsNotNullField(description = "更新时间")
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    
    private Date updateTime;





}
