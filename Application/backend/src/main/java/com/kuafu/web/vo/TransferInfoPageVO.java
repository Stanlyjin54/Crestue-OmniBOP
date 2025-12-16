package com.kuafu.web.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kuafu.common.deserializer.MultiDateDeserializer;
import com.kuafu.common.domin.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;
import com.kuafu.common.entity.StaticResource;
import com.kuafu.common.entity.BaseEntity;
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
public class TransferInfoPageVO extends BaseEntity {

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
