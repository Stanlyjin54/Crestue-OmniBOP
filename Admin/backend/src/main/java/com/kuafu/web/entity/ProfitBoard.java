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
 * <p>  利润看板 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("profit_board")
public class ProfitBoard    {
    @TableId(value = "profit_board_id", type = IdType.AUTO)
    @JsonProperty(value = "profitBoardId")
    @Excel(name = "主键")

    private Integer profitBoardId;
    @JsonProperty(value = "targetType")
    @Excel(name = "维度类型")
    @ExcelProperty(value = "维度类型")
    @TableField(value = "target_type")

    private String targetType;
    @JsonProperty(value = "targetId")
    @Excel(name = "维度ID")
    @ExcelProperty(value = "维度ID")
    @TableField(value = "target_id")

    private Integer targetId;
    @JsonProperty(value = "profitAmount")
    @Excel(name = "毛利金额")
    @ExcelProperty(value = "毛利金额")
    @TableField(value = "profit_amount")

    private Double profitAmount;
    @JsonProperty(value = "commission")
    @Excel(name = "平台佣金")
    @ExcelProperty(value = "平台佣金")
    @TableField(value = "commission")

    private Double commission;
    @JsonProperty(value = "shippingFee")
    @Excel(name = "运费")
    @ExcelProperty(value = "运费")
    @TableField(value = "shipping_fee")

    private Double shippingFee;
    @JsonProperty(value = "statDate")
    @Excel(name = "统计日期")
    @ExcelProperty(value = "统计日期")
    @TableField(value = "stat_date")

    private Date statDate;


}





