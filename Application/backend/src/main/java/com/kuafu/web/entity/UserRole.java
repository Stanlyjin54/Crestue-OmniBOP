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
 * <p>  用户角色关联 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_role")
public class UserRole  {
    @TableId(value = "user_role_id", type = IdType.AUTO)
    @JsonProperty("user_role_id")
    
    
    
    private Integer userRoleId;
    @JsonProperty("user_info_id")
    @IsNotNullField(description = "用户ID")
    
    
    @TableField("user_info_id")
    private Integer userInfoId;
    @JsonProperty("role_id")
    @IsNotNullField(description = "角色ID")
    
    
    @TableField("role_id")
    private Integer roleId;





}
