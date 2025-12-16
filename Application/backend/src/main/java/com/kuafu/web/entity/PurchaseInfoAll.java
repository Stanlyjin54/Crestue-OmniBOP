package com.kuafu.web.entity;

import java.util.Date;
import java.math.BigDecimal;
import java.util.List;
import com.kuafu.common.entity.StaticResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



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
public class PurchaseInfoAll {
    @TableField(value="pi.purchase_no")
    @JsonProperty("purchase_no")
    private String purchaseNo;
    @TableField(value="pi.purchase_info_id")
    @JsonProperty("purchase_info_id")
    private Integer purchaseInfoId;
    @TableField(value="pi.purchase_status_enum_purchase_status_enum_id_1")
    @JsonProperty("purchase_status_enum_purchase_status_enum_id_1")
    private Integer purchaseStatusEnumPurchaseStatusEnumId1;
    @TableField(value="ui.password")
    @JsonProperty("password")
    private String password;
    @TableField(value="pi.user_info_user_info_id_1")
    @JsonProperty("user_info_user_info_id_1")
    private Integer userInfoUserInfoId1;
    @TableField(value="ui.registration_date")
    @JsonProperty("registration_date")
    private Date registrationDate;
    @TableField(value="pi.total_amount")
    @JsonProperty("total_amount")
    private Double totalAmount;
    @TableField(value="pse.status_name")
    @JsonProperty("status_name")
    private String statusName;
    @TableField(value="pi.supplier")
    @JsonProperty("supplier")
    private String supplier;
    @TableField(value="ui.phone_number")
    @JsonProperty("phone_number")
    private String phoneNumber;
    @TableField(value="pi.purchase_date")
    @JsonProperty("purchase_date")
    private Date purchaseDate;
    @TableField(value="ui.username")
    @JsonProperty("username")
    private String username;









}
