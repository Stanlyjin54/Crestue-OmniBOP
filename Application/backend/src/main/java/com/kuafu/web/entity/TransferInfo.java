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
 * <p>  调拨信息 </p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("transfer_info")
public class TransferInfo  {
    @TableId(value = "transfer_info_id", type = IdType.AUTO)
    @JsonProperty("transfer_info_id")
    
    
    
    private Integer transferInfoId;
    @JsonProperty("from_warehouse_info_warehouse_info_id_1")
    @IsNotNullField(description = "调出仓库ID")
    
    
    @TableField("from_warehouse_info_warehouse_info_id_1")
    private Integer fromWarehouseInfoWarehouseInfoId1;
    @JsonProperty("to_warehouse_info_warehouse_info_id_2")
    @IsNotNullField(description = "调入仓库ID")
    
    
    @TableField("to_warehouse_info_warehouse_info_id_2")
    private Integer toWarehouseInfoWarehouseInfoId2;
    @JsonProperty("product_info_product_info_id_1")
    @IsNotNullField(description = "商品ID")
    
    
    @TableField("product_info_product_info_id_1")
    private Integer productInfoProductInfoId1;
    @JsonProperty("quantity")
    @IsNotNullField(description = "调拨数量")
    
    
    @TableField("quantity")
    private Integer quantity;
    @JsonProperty("transfer_status_enum_transfer_status_enum_id_1")
    @IsNotNullField(description = "调拨状态")
    
    
    @TableField("transfer_status_enum_transfer_status_enum_id_1")
    private Integer transferStatusEnumTransferStatusEnumId1;
    @JsonProperty("apply_user_info_user_info_id_1")
    @IsNotNullField(description = "申请人ID")
    
    
    @TableField("apply_user_info_user_info_id_1")
    private Integer applyUserInfoUserInfoId1;
    @JsonProperty("confirm_user_info_user_info_id_2")
    
    
    @TableField("confirm_user_info_user_info_id_2")
    private Integer confirmUserInfoUserInfoId2;
    @JsonProperty("apply_date")
    @IsNotNullField(description = "申请时间")
    
    
    @TableField("apply_date")
    private Date applyDate;
    @JsonProperty("confirm_date")
    
    
    @TableField("confirm_date")
    private Date confirmDate;





}
