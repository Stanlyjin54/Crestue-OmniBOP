package com.kuafuweb.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;

/**
 * <p>调拨信息</p>
 *
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferInfoVO  {

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





