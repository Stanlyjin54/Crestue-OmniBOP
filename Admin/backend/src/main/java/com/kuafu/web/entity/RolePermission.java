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
public class RolePermission    {
    @TableId(value = "role_permission_id", type = IdType.AUTO)
    @JsonProperty(value = "rolePermissionId")
    @Excel(name = "主键")

    private Integer rolePermissionId;
    @JsonProperty(value = "roleId")
    @Excel(name = "角色ID")
    @ExcelProperty(value = "角色ID")
    @TableField(value = "role_id")

    private Integer roleId;
    @JsonProperty(value = "permissionId")
    @Excel(name = "权限ID")
    @ExcelProperty(value = "权限ID")
    @TableField(value = "permission_id")

    private Integer permissionId;


}





