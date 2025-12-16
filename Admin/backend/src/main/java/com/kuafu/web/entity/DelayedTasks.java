package com.kuafuweb.web.entity;

import java.util.Date;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kuafuweb.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;
import com.kuafuweb.common.annotation.Excel.*;



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
public class DelayedTasks    {
    @TableId(value = "id", type = IdType.AUTO)
    @JsonProperty(value = "id")
    @Excel(name = "主键")

    private Integer id;
    @JsonProperty(value = "taskType")
    @Excel(name = "任务类型")
    @ExcelProperty(value = "任务类型")
    @TableField(value = "task_type")

    private String taskType;
    @JsonProperty(value = "taskData")
    @Excel(name = "任务数据")
    @ExcelProperty(value = "任务数据")
    @TableField(value = "task_data")

    private String taskData;
    @JsonProperty(value = "status")
    @Excel(name = "任务状态")
    @ExcelProperty(value = "任务状态")
    @TableField(value = "status")

    private String status;
    @JsonProperty(value = "executeTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "执行时间")
    @ExcelProperty(value = "执行时间")
    @TableField(value = "execute_time")

    private Date executeTime;
    @JsonProperty(value = "createTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "创建时间")
    @ExcelProperty(value = "创建时间")
    @TableField(value = "create_time")

    private Date createTime;
    @JsonProperty(value = "updateTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "更新时间")
    @ExcelProperty(value = "更新时间")
    @TableField(value = "update_time")

    private Date updateTime;


}





