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
public class PermissionLog  {
    @TableId(value = "permission_log_id", type = IdType.AUTO)
    @JsonProperty("permission_log_id")
    
    
    
    private Integer permissionLogId;
    @JsonProperty("user_info_id")
    @IsNotNullField(description = "操作人")
    
    
    @TableField("user_info_id")
    private Integer userInfoId;
    @JsonProperty("action_type")
    @IsNotNullField(description = "操作类型")
    
    
    @TableField("action_type")
    private String actionType;
    @JsonProperty("target_object")
    @IsNotNullField(description = "目标对象")
    
    
    @TableField("target_object")
    private String targetObject;
    @JsonProperty("change_content")
    
    
    @TableField("change_content")
    private String changeContent;
    @JsonProperty("action_time")
    @IsNotNullField(description = "操作时间")
    
    
    @TableField("action_time")
    private Date actionTime;





}
