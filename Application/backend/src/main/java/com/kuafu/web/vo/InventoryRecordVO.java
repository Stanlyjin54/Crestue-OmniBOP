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
 * <p>库存记录</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRecordVO  extends BaseEntity {

    @JsonProperty("inventoryRecordId")
    private Integer inventoryRecordId;
    @JsonProperty("productInfoProductInfoId1")
    private Integer productInfoProductInfoId1;
    @JsonProperty("warehouseInfoWarehouseInfoId1")
    private Integer warehouseInfoWarehouseInfoId1;
    @JsonProperty("quantity")
    private Integer quantity;
    @JsonProperty("batchNumber")
    private String batchNumber;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("expireDate")
    private Date expireDate;
    @JsonProperty("serialNumber")
    private String serialNumber;




}
