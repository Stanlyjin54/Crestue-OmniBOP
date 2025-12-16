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
public class RolePermissionAll {
    @TableField(value="r.role_name")
    @JsonProperty("role_name")
    private String roleName;
    @TableField(value="p.permission_desc")
    @JsonProperty("permission_desc")
    private String permissionDesc;
    @TableField(value="r.create_time")
    @JsonProperty("create_time")
    private Date createTime;
    @TableField(value="rp.role_id")
    @JsonProperty("role_id")
    private Integer roleId;
    @TableField(value="r.role_desc")
    @JsonProperty("role_desc")
    private String roleDesc;
    @TableField(value="rp.permission_id")
    @JsonProperty("permission_id")
    private Integer permissionId;
    @TableField(value="p.permission_name")
    @JsonProperty("permission_name")
    private String permissionName;
    @TableField(value="p.feature_code")
    @JsonProperty("feature_code")
    private String featureCode;
    @TableField(value="rp.role_permission_id")
    @JsonProperty("role_permission_id")
    private Integer rolePermissionId;









}
