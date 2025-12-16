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
 * <p>  角色权限关联 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("role_permission")
public class RolePermission  {
    @TableId(value = "role_permission_id", type = IdType.AUTO)
    @JsonProperty("role_permission_id")
    
    
    
    private Integer rolePermissionId;
    @JsonProperty("role_id")
    @IsNotNullField(description = "角色ID")
    
    
    @TableField("role_id")
    private Integer roleId;
    @JsonProperty("permission_id")
    @IsNotNullField(description = "权限ID")
    
    
    @TableField("permission_id")
    private Integer permissionId;





}
