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
public class TransferInfoAll {
    @TableField(value="ti.transfer_status_enum_transfer_status_enum_id_1")
    @JsonProperty("transfer_status_enum_transfer_status_enum_id_1")
    private Integer transferStatusEnumTransferStatusEnumId1;
    @TableField(value="ti.from_warehouse_info_warehouse_info_id_1")
    @JsonProperty("from_warehouse_info_warehouse_info_id_1")
    private Integer fromWarehouseInfoWarehouseInfoId1;
    @TableField(value="pi.shelf_life")
    @JsonProperty("shelf_life")
    private Date shelfLife;
    @TableField(value="ti.apply_date")
    @JsonProperty("apply_date")
    private Date applyDate;
    @TableField(value="ui.password")
    @JsonProperty("password")
    private String password;
    @TableField(value="pi.barcode")
    @JsonProperty("barcode")
    private String barcode;
    @TableField(value="pi.brand")
    @JsonProperty("brand")
    private String brand;
    @TableField(value="ti.quantity")
    @JsonProperty("quantity")
    private Integer quantity;
    @TableField(value="pi.product_status_enum_product_status_enum_id_1")
    @JsonProperty("product_status_enum_product_status_enum_id_1")
    private Integer productStatusEnumProductStatusEnumId1;
    @TableField(value="tse.status_name")
    @JsonProperty("status_name")
    private String statusName;
    @TableField(value="ti.apply_user_info_user_info_id_1")
    @JsonProperty("apply_user_info_user_info_id_1")
    private Integer applyUserInfoUserInfoId1;
    @TableField(value="pi.specification")
    @JsonProperty("specification")
    private String specification;
    @TableField(value="pi.serial_number")
    @JsonProperty("serial_number")
    private String serialNumber;
    @TableField(value="ti.confirm_user_info_user_info_id_2")
    @JsonProperty("confirm_user_info_user_info_id_2")
    private Integer confirmUserInfoUserInfoId2;
    @TableField(value="ti.product_info_product_info_id_1")
    @JsonProperty("product_info_product_info_id_1")
    private Integer productInfoProductInfoId1;
    @TableField(value="pi.product_name")
    @JsonProperty("product_name")
    private String productName;
    @TableField(value="pi.unit")
    @JsonProperty("unit")
    private String unit;
    @TableField(value="wi.warehouse_name")
    @JsonProperty("warehouse_name")
    private String warehouseName;
    @TableField(value="ui.registration_date")
    @JsonProperty("registration_date")
    private Date registrationDate;
    @TableField(value="ti.to_warehouse_info_warehouse_info_id_2")
    @JsonProperty("to_warehouse_info_warehouse_info_id_2")
    private Integer toWarehouseInfoWarehouseInfoId2;
    @TableField(value="ti.confirm_date")
    @JsonProperty("confirm_date")
    private Date confirmDate;
    @TableField(value="wi.location")
    @JsonProperty("location")
    private String location;
    @TableField(value="pi.min_stock")
    @JsonProperty("min_stock")
    private Integer minStock;
    @TableField(value="ui.phone_number")
    @JsonProperty("phone_number")
    private String phoneNumber;
    @TableField(value="pi.category")
    @JsonProperty("category")
    private String category;
    @TableField(value="ti.transfer_info_id")
    @JsonProperty("transfer_info_id")
    private Integer transferInfoId;
    @TableField(value="ui.username")
    @JsonProperty("username")
    private String username;









}
