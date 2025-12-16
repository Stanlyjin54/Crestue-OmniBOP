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
public class FissionTool    {
    @TableId(value = "fission_tool_id", type = IdType.AUTO)
    @JsonProperty(value = "fissionToolId")
    @Excel(name = "主键")

    private Integer fissionToolId;
    @JsonProperty(value = "toolType")
    @Excel(name = "工具类型")
    @ExcelProperty(value = "工具类型")
    @TableField(value = "tool_type")

    private String toolType;
    @JsonProperty(value = "title")
    @Excel(name = "活动标题")
    @ExcelProperty(value = "活动标题")
    @TableField(value = "title")

    private String title;
    @JsonProperty(value = "config")
    @Excel(name = "配置内容")
    @ExcelProperty(value = "配置内容")
    @TableField(value = "config")

    private String config;
    @JsonProperty(value = "startDate")
    @Excel(name = "开始日期")
    @ExcelProperty(value = "开始日期")
    @TableField(value = "start_date")

    private Date startDate;
    @JsonProperty(value = "endDate")
    @Excel(name = "结束日期")
    @ExcelProperty(value = "结束日期")
    @TableField(value = "end_date")

    private Date endDate;


}





