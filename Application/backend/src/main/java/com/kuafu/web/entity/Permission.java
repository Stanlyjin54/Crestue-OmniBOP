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
public class Permission  {
    @TableId(value = "permission_id", type = IdType.AUTO)
    @JsonProperty("permission_id")
    
    
    
    private Integer permissionId;
    @JsonProperty("permission_name")
    @IsNotNullField(description = "权限名称")
    
    
    @TableField("permission_name")
    private String permissionName;
    @JsonProperty("permission_desc")
    
    
    @TableField("permission_desc")
    private String permissionDesc;
    @JsonProperty("feature_code")
    @IsNotNullField(description = "功能标识")
    
    
    @TableField("feature_code")
    private String featureCode;





}
