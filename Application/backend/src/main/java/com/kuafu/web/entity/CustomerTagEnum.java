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
 * <p>  客户标签枚举 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("customer_tag_enum")
public class CustomerTagEnum  {
    @TableId(value = "customer_tag_enum_id", type = IdType.AUTO)
    @JsonProperty("customer_tag_enum_id")
    
    
    
    private Integer customerTagEnumId;
    @JsonProperty("tag_name")
    @IsNotNullField(description = "标签名称")
    
    
    @TableField("tag_name")
    private String tagName;
    @JsonProperty("is_ai_generated")
    
    
    @TableField("is_ai_generated")
    private Boolean isAiGenerated;





}
