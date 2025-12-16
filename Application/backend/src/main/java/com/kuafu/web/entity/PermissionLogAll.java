package com.kuafu.web.entity;

import java.util.Date;
import java.math.BigDecimal;
import java.util.List;
import com.kuafu.common.entity.StaticResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



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
public class PermissionLogAll {
    @TableField(value="ui.password")
    @JsonProperty("password")
    private String password;
    @TableField(value="pl.action_time")
    @JsonProperty("action_time")
    private Date actionTime;
    @TableField(value="ui.registration_date")
    @JsonProperty("registration_date")
    private Date registrationDate;
    @TableField(value="pl.action_type")
    @JsonProperty("action_type")
    private String actionType;
    @TableField(value="pl.target_object")
    @JsonProperty("target_object")
    private String targetObject;
    @TableField(value="ui.phone_number")
    @JsonProperty("phone_number")
    private String phoneNumber;
    @TableField(value="pl.permission_log_id")
    @JsonProperty("permission_log_id")
    private Integer permissionLogId;
    @TableField(value="pl.user_info_id")
    @JsonProperty("user_info_id")
    private Integer userInfoId;
    @TableField(value="pl.change_content")
    @JsonProperty("change_content")
    private String changeContent;
    @TableField(value="ui.username")
    @JsonProperty("username")
    private String username;









}
