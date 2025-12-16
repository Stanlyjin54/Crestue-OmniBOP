package com.kuafu.web.vo;

import com.kuafu.common.domin.PageRequest;
import com.kuafu.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kuafu.common.deserializer.MultiDateDeserializer;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;
import com.kuafu.common.entity.StaticResource;

/**
 * <p>调拨信息-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TransferInfoAllPageVO extends BaseEntity {

@JsonProperty("transferStatusEnumTransferStatusEnumId1")
    private Integer transferStatusEnumTransferStatusEnumId1;
@JsonProperty("warehouseNameWi1")
    private String warehouseNameWi1;
@JsonProperty("fromWarehouseInfoWarehouseInfoId1")
    private Integer fromWarehouseInfoWarehouseInfoId1;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("shelfLife")
    private Date shelfLife;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("applyDate")
    private Date applyDate;
@JsonProperty("password")
    private String password;
@JsonProperty("barcode")
    private String barcode;
@JsonProperty("brand")
    private String brand;
@JsonProperty("phoneNumberUi1")
    private String phoneNumberUi1;
@JsonProperty("locationWi1")
    private String locationWi1;
@JsonProperty("quantity")
    private Integer quantity;
@JsonProperty("productStatusEnumProductStatusEnumId1")
    private Integer productStatusEnumProductStatusEnumId1;
@JsonProperty("statusName")
    private String statusName;
@JsonProperty("applyUserInfoUserInfoId1")
    private Integer applyUserInfoUserInfoId1;
@JsonProperty("passwordUi1")
    private String passwordUi1;
@JsonProperty("specification")
    private String specification;
@JsonProperty("serialNumber")
    private String serialNumber;
@JsonProperty("confirmUserInfoUserInfoId2")
    private Integer confirmUserInfoUserInfoId2;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("registrationDateUi1")
    private Date registrationDateUi1;
@JsonProperty("productInfoProductInfoId1")
    private Integer productInfoProductInfoId1;
@JsonProperty("productName")
    private String productName;
@JsonProperty("usernameUi1")
    private String usernameUi1;
@JsonProperty("unit")
    private String unit;
@JsonProperty("warehouseName")
    private String warehouseName;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("registrationDate")
    private Date registrationDate;
@JsonProperty("toWarehouseInfoWarehouseInfoId2")
    private Integer toWarehouseInfoWarehouseInfoId2;
    @JsonDeserialize(using = MultiDateDeserializer.class)
@JsonProperty("confirmDate")
    private Date confirmDate;
@JsonProperty("location")
    private String location;
@JsonProperty("minStock")
    private Integer minStock;
@JsonProperty("phoneNumber")
    private String phoneNumber;
@JsonProperty("category")
    private String category;
@JsonProperty("transferInfoId")
    private Integer transferInfoId;
@JsonProperty("username")
    private String username;

}
