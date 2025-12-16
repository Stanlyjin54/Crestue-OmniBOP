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
 * <p>  权限变更日志 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("permission_log")
public class PermissionLog    {
    @TableId(value = "permission_log_id", type = IdType.AUTO)
    @JsonProperty(value = "permissionLogId")
    @Excel(name = "主键")

    private Integer permissionLogId;
    @JsonProperty(value = "userInfoId")
    @Excel(name = "操作人")
    @ExcelProperty(value = "操作人")
    @TableField(value = "user_info_id")

    private Integer userInfoId;
    @JsonProperty(value = "actionType")
    @Excel(name = "操作类型")
    @ExcelProperty(value = "操作类型")
    @TableField(value = "action_type")

    private String actionType;
    @JsonProperty(value = "targetObject")
    @Excel(name = "目标对象")
    @ExcelProperty(value = "目标对象")
    @TableField(value = "target_object")

    private String targetObject;
    @JsonProperty(value = "changeContent")
    @Excel(name = "变更内容")
    @ExcelProperty(value = "变更内容")
    @TableField(value = "change_content")

    private String changeContent;
    @JsonProperty(value = "actionTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "操作时间")
    @ExcelProperty(value = "操作时间")
    @TableField(value = "action_time")

    private Date actionTime;


}





