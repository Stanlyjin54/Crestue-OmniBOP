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
 * <p>  采购状态枚举 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("purchase_status_enum")
public class PurchaseStatusEnum    {
    @TableId(value = "purchase_status_enum_id", type = IdType.AUTO)
    @JsonProperty(value = "purchaseStatusEnumId")
    @Excel(name = "主键")

    private Integer purchaseStatusEnumId;
    @JsonProperty(value = "statusName")
    @Excel(name = "状态名称")
    @ExcelProperty(value = "状态名称")
    @TableField(value = "status_name")

    private String statusName;


}





