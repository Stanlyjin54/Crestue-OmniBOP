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
 * <p>  权限 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("permission")
public class Permission    {
    @TableId(value = "permission_id", type = IdType.AUTO)
    @JsonProperty(value = "permissionId")
    @Excel(name = "主键")

    private Integer permissionId;
    @JsonProperty(value = "permissionName")
    @Excel(name = "权限名称")
    @ExcelProperty(value = "权限名称")
    @TableField(value = "permission_name")

    private String permissionName;
    @JsonProperty(value = "permissionDesc")
    @Excel(name = "权限描述")
    @ExcelProperty(value = "权限描述")
    @TableField(value = "permission_desc")

    private String permissionDesc;
    @JsonProperty(value = "featureCode")
    @Excel(name = "功能标识")
    @ExcelProperty(value = "功能标识")
    @TableField(value = "feature_code")

    private String featureCode;


}





