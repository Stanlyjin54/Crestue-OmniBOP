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
 * <p>  采购信息 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("purchase_info")
public class PurchaseInfo    {
    @TableId(value = "purchase_info_id", type = IdType.AUTO)
    @JsonProperty(value = "purchaseInfoId")
    @Excel(name = "主键")

    private Integer purchaseInfoId;
    @JsonProperty(value = "purchaseNo")
    @Excel(name = "采购单号")
    @ExcelProperty(value = "采购单号")
    @TableField(value = "purchase_no")

    private String purchaseNo;
    @JsonProperty(value = "userInfoUserInfoId1")
    @Excel(name = "采购员ID")
    @ExcelProperty(value = "采购员ID")
    @TableField(value = "user_info_user_info_id_1")

    private Integer userInfoUserInfoId1;
    @JsonProperty(value = "supplier")
    @Excel(name = "供应商")
    @ExcelProperty(value = "供应商")
    @TableField(value = "supplier")

    private String supplier;
    @JsonProperty(value = "totalAmount")
    @Excel(name = "采购总金额")
    @ExcelProperty(value = "采购总金额")
    @TableField(value = "total_amount")

    private Double totalAmount;
    @JsonProperty(value = "purchaseDate")
    @Excel(name = "采购时间")
    @ExcelProperty(value = "采购时间")
    @TableField(value = "purchase_date")

    private Date purchaseDate;
    @JsonProperty(value = "purchaseStatusEnumPurchaseStatusEnumId1")
    @Excel(name = "采购状态")
    @ExcelProperty(value = "采购状态")
    @TableField(value = "purchase_status_enum_purchase_status_enum_id_1")

    private Integer purchaseStatusEnumPurchaseStatusEnumId1;


}





