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
public class RfmLevelEnum    {
    @TableId(value = "rfm_level_enum_id", type = IdType.AUTO)
    @JsonProperty(value = "rfmLevelEnumId")
    @Excel(name = "主键")

    private Integer rfmLevelEnumId;
    @JsonProperty(value = "levelName")
    @Excel(name = "分层名称")
    @ExcelProperty(value = "分层名称")
    @TableField(value = "level_name")

    private String levelName;


}





