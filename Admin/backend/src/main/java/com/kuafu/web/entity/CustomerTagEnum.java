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
public class CustomerTagEnum    {
    @TableId(value = "customer_tag_enum_id", type = IdType.AUTO)
    @JsonProperty(value = "customerTagEnumId")
    @Excel(name = "主键")

    private Integer customerTagEnumId;
    @JsonProperty(value = "tagName")
    @Excel(name = "标签名称")
    @ExcelProperty(value = "标签名称")
    @TableField(value = "tag_name")

    private String tagName;
    @JsonProperty(value = "isAiGenerated")
    @Excel(name = "是否AI生成")
    @ExcelProperty(value = "是否AI生成")
    @TableField(value = "is_ai_generated")

    private Boolean isAiGenerated;


}





