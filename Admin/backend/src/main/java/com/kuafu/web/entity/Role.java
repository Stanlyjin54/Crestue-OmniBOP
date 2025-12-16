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
 * <p>  角色 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("role")
public class Role    {
    @TableId(value = "role_id", type = IdType.AUTO)
    @JsonProperty(value = "roleId")
    @Excel(name = "主键")

    private Integer roleId;
    @JsonProperty(value = "roleName")
    @Excel(name = "角色名称")
    @ExcelProperty(value = "角色名称")
    @TableField(value = "role_name")

    private String roleName;
    @JsonProperty(value = "roleDesc")
    @Excel(name = "角色描述")
    @ExcelProperty(value = "角色描述")
    @TableField(value = "role_desc")

    private String roleDesc;
    @JsonProperty(value = "createTime")
    @Excel(dateFormat = "yyyy-MM-dd HH:mm:ss", name = "创建时间")
    @ExcelProperty(value = "创建时间")
    @TableField(value = "create_time")

    private Date createTime;


}





