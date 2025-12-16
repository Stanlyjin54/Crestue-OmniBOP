package com.kuafu.web.vo;

import lombok.AllArgsConstructor;
import com.kuafu.common.entity.StaticResource;
import java.util.List;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kuafu.common.deserializer.MultiDateDeserializer;
import com.kuafu.common.entity.BaseEntity;
/**
 * <p>调拨信息</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferInfoVO  extends BaseEntity {

    @JsonProperty("transferInfoId")
    private Integer transferInfoId;
    @JsonProperty("fromWarehouseInfoWarehouseInfoId1")
    private Integer fromWarehouseInfoWarehouseInfoId1;
    @JsonProperty("toWarehouseInfoWarehouseInfoId2")
    private Integer toWarehouseInfoWarehouseInfoId2;
    @JsonProperty("productInfoProductInfoId1")
    private Integer productInfoProductInfoId1;
    @JsonProperty("quantity")
    private Integer quantity;
    @JsonProperty("transferStatusEnumTransferStatusEnumId1")
    private Integer transferStatusEnumTransferStatusEnumId1;
    @JsonProperty("applyUserInfoUserInfoId1")
    private Integer applyUserInfoUserInfoId1;
    @JsonProperty("confirmUserInfoUserInfoId2")
    private Integer confirmUserInfoUserInfoId2;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("applyDate")
    private Date applyDate;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("confirmDate")
    private Date confirmDate;




}
