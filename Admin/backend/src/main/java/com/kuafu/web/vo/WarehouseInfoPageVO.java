package com.kuafuweb.web.vo;

import java.util.Date;

import com.kuafuweb.common.domin.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>仓库信息-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WarehouseInfoPageVO extends PageRequest {

    @JsonProperty(value = "warehouseInfoId")
    private Integer warehouseInfoId;
    @JsonProperty(value = "warehouseName")
    private String warehouseName;
    @JsonProperty(value = "location")
    private String location;

}





