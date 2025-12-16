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
public class PurchaseInfo  {
    @TableId(value = "purchase_info_id", type = IdType.AUTO)
    @JsonProperty("purchase_info_id")
    
    
    
    private Integer purchaseInfoId;
    @JsonProperty("purchase_no")
    @IsNotNullField(description = "采购单号")
    
    
    @TableField("purchase_no")
    private String purchaseNo;
    @JsonProperty("user_info_user_info_id_1")
    @IsNotNullField(description = "采购员ID")
    
    
    @TableField("user_info_user_info_id_1")
    private Integer userInfoUserInfoId1;
    @JsonProperty("supplier")
    
    
    @TableField("supplier")
    private String supplier;
    @JsonProperty("total_amount")
    @IsNotNullField(description = "采购总金额")
    
    
    @TableField("total_amount")
    private Double totalAmount;
    @JsonProperty("purchase_date")
    @IsNotNullField(description = "采购时间")
    
    
    @TableField("purchase_date")
    private Date purchaseDate;
    @JsonProperty("purchase_status_enum_purchase_status_enum_id_1")
    @IsNotNullField(description = "采购状态")
    
    
    @TableField("purchase_status_enum_purchase_status_enum_id_1")
    private Integer purchaseStatusEnumPurchaseStatusEnumId1;





}
