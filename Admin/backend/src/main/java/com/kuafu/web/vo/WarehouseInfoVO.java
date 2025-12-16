package com.kuafuweb.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;

/**
 * <p>仓库信息</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseInfoVO  {

     @JsonProperty(value = "warehouseInfoId")
    private Integer warehouseInfoId;
     @JsonProperty(value = "warehouseName")
    private String warehouseName;
     @JsonProperty(value = "location")
    private String location;


}





