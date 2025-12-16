package com.kuafuweb.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;

/**
 * <p>库存记录</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRecordVO  {

     @JsonProperty(value = "inventoryRecordId")
    private Integer inventoryRecordId;
     @JsonProperty(value = "productInfoProductInfoId1")
    private Integer productInfoProductInfoId1;
     @JsonProperty(value = "warehouseInfoWarehouseInfoId1")
    private Integer warehouseInfoWarehouseInfoId1;
     @JsonProperty(value = "quantity")
    private Integer quantity;
     @JsonProperty(value = "batchNumber")
    private String batchNumber;
     @JsonProperty(value = "expireDate")
    private Date expireDate;
     @JsonProperty(value = "serialNumber")
    private String serialNumber;


}





