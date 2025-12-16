package com.kuafuweb.web.vo;

import java.util.Date;

import com.kuafuweb.common.domin.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
public class TransferInfoPageVO extends PageRequest {

    @JsonProperty(value = "transferInfoId")
    private Integer transferInfoId;
    @JsonProperty(value = "fromWarehouseInfoWarehouseInfoId1")
    private Integer fromWarehouseInfoWarehouseInfoId1;
    @JsonProperty(value = "toWarehouseInfoWarehouseInfoId2")
    private Integer toWarehouseInfoWarehouseInfoId2;
    @JsonProperty(value = "productInfoProductInfoId1")
    private Integer productInfoProductInfoId1;
    @JsonProperty(value = "quantity")
    private Integer quantity;
    @JsonProperty(value = "transferStatusEnumTransferStatusEnumId1")
    private Integer transferStatusEnumTransferStatusEnumId1;
    @JsonProperty(value = "applyUserInfoUserInfoId1")
    private Integer applyUserInfoUserInfoId1;
    @JsonProperty(value = "confirmUserInfoUserInfoId2")
    private Integer confirmUserInfoUserInfoId2;
    @JsonProperty(value = "applyDate")
    private Date applyDate;
    @JsonProperty(value = "confirmDate")
    private Date confirmDate;

}





