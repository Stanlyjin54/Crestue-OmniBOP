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
public class Role  {
    @TableId(value = "role_id", type = IdType.AUTO)
    @JsonProperty("role_id")
    
    
    
    private Integer roleId;
    @JsonProperty("role_name")
    @IsNotNullField(description = "角色名称")
    
    
    @TableField("role_name")
    private String roleName;
    @JsonProperty("role_desc")
    
    
    @TableField("role_desc")
    private String roleDesc;
    @JsonProperty("create_time")
    @TableField(fill = FieldFill.INSERT)
    
    
    private Date createTime;





}
