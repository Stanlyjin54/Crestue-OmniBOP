package com.kuafuweb.web.vo;

import java.util.Date;

import com.kuafuweb.common.domin.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>库存记录-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InventoryRecordPageVO extends PageRequest {

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





