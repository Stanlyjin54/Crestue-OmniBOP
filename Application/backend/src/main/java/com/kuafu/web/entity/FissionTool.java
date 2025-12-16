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
 * <p>  裂变工具 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("fission_tool")
public class FissionTool  {
    @TableId(value = "fission_tool_id", type = IdType.AUTO)
    @JsonProperty("fission_tool_id")
    
    
    
    private Integer fissionToolId;
    @JsonProperty("tool_type")
    @IsNotNullField(description = "工具类型")
    
    
    @TableField("tool_type")
    private String toolType;
    @JsonProperty("title")
    @IsNotNullField(description = "活动标题")
    
    
    @TableField("title")
    private String title;
    @JsonProperty("config")
    
    
    @TableField("config")
    private String config;
    @JsonProperty("start_date")
    @IsNotNullField(description = "开始日期")
    
    
    @TableField("start_date")
    private Date startDate;
    @JsonProperty("end_date")
    @IsNotNullField(description = "结束日期")
    
    
    @TableField("end_date")
    private Date endDate;





}
