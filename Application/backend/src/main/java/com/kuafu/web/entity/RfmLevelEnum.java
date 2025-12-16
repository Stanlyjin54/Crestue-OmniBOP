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
 * <p>  RFM分层枚举 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("rfm_level_enum")
public class RfmLevelEnum  {
    @TableId(value = "rfm_level_enum_id", type = IdType.AUTO)
    @JsonProperty("rfm_level_enum_id")
    
    
    
    private Integer rfmLevelEnumId;
    @JsonProperty("level_name")
    @IsNotNullField(description = "分层名称")
    
    
    @TableField("level_name")
    private String levelName;





}
