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
public class UserRole    {
    @TableId(value = "user_role_id", type = IdType.AUTO)
    @JsonProperty(value = "userRoleId")
    @Excel(name = "主键")

    private Integer userRoleId;
    @JsonProperty(value = "userInfoId")
    @Excel(name = "用户ID")
    @ExcelProperty(value = "用户ID")
    @TableField(value = "user_info_id")

    private Integer userInfoId;
    @JsonProperty(value = "roleId")
    @Excel(name = "角色ID")
    @ExcelProperty(value = "角色ID")
    @TableField(value = "role_id")

    private Integer roleId;


}





