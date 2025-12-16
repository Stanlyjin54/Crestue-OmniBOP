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
public class TransferInfo    {
    @TableId(value = "transfer_info_id", type = IdType.AUTO)
    @JsonProperty(value = "transferInfoId")
    @Excel(name = "主键")

    private Integer transferInfoId;
    @JsonProperty(value = "fromWarehouseInfoWarehouseInfoId1")
    @Excel(name = "调出仓库ID")
    @ExcelProperty(value = "调出仓库ID")
    @TableField(value = "from_warehouse_info_warehouse_info_id_1")

    private Integer fromWarehouseInfoWarehouseInfoId1;
    @JsonProperty(value = "toWarehouseInfoWarehouseInfoId2")
    @Excel(name = "调入仓库ID")
    @ExcelProperty(value = "调入仓库ID")
    @TableField(value = "to_warehouse_info_warehouse_info_id_2")

    private Integer toWarehouseInfoWarehouseInfoId2;
    @JsonProperty(value = "productInfoProductInfoId1")
    @Excel(name = "商品ID")
    @ExcelProperty(value = "商品ID")
    @TableField(value = "product_info_product_info_id_1")

    private Integer productInfoProductInfoId1;
    @JsonProperty(value = "quantity")
    @Excel(name = "调拨数量")
    @ExcelProperty(value = "调拨数量")
    @TableField(value = "quantity")

    private Integer quantity;
    @JsonProperty(value = "transferStatusEnumTransferStatusEnumId1")
    @Excel(name = "调拨状态")
    @ExcelProperty(value = "调拨状态")
    @TableField(value = "transfer_status_enum_transfer_status_enum_id_1")

    private Integer transferStatusEnumTransferStatusEnumId1;
    @JsonProperty(value = "applyUserInfoUserInfoId1")
    @Excel(name = "申请人ID")
    @ExcelProperty(value = "申请人ID")
    @TableField(value = "apply_user_info_user_info_id_1")

    private Integer applyUserInfoUserInfoId1;
    @JsonProperty(value = "confirmUserInfoUserInfoId2")
    @Excel(name = "确认人ID")
    @ExcelProperty(value = "确认人ID")
    @TableField(value = "confirm_user_info_user_info_id_2")

    private Integer confirmUserInfoUserInfoId2;
    @JsonProperty(value = "applyDate")
    @Excel(name = "申请时间")
    @ExcelProperty(value = "申请时间")
    @TableField(value = "apply_date")

    private Date applyDate;
    @JsonProperty(value = "confirmDate")
    @Excel(name = "确认时间")
    @ExcelProperty(value = "确认时间")
    @TableField(value = "confirm_date")

    private Date confirmDate;


}





