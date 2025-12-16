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
 * <p>  采购建议 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("procurement_suggestion")
public class ProcurementSuggestion    {
    @TableId(value = "procurement_suggestion_id", type = IdType.AUTO)
    @JsonProperty(value = "procurementSuggestionId")
    @Excel(name = "主键")

    private Integer procurementSuggestionId;
    @JsonProperty(value = "productInfoProductInfoId1")
    @Excel(name = "商品ID")
    @ExcelProperty(value = "商品ID")
    @TableField(value = "product_info_product_info_id_1")

    private Integer productInfoProductInfoId1;
    @JsonProperty(value = "suggestQuantity")
    @Excel(name = "建议采购数量")
    @ExcelProperty(value = "建议采购数量")
    @TableField(value = "suggest_quantity")

    private Integer suggestQuantity;
    @JsonProperty(value = "reason")
    @Excel(name = "建议原因")
    @ExcelProperty(value = "建议原因")
    @TableField(value = "reason")

    private String reason;
    @JsonProperty(value = "suggestDate")
    @Excel(name = "建议日期")
    @ExcelProperty(value = "建议日期")
    @TableField(value = "suggest_date")

    private Date suggestDate;


}





