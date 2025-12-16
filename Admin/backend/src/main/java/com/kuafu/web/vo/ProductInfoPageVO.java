package com.kuafuweb.web.vo;

import java.util.Date;

import com.kuafuweb.common.domin.PageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>商品信息-分页列表-响应参数</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductInfoPageVO extends PageRequest {

    @JsonProperty(value = "productInfoId")
    private Integer productInfoId;
    @JsonProperty(value = "productName")
    private String productName;
    @JsonProperty(value = "barcode")
    private String barcode;
    @JsonProperty(value = "category")
    private String category;
    @JsonProperty(value = "brand")
    private String brand;
    @JsonProperty(value = "specification")
    private String specification;
    @JsonProperty(value = "unit")
    private String unit;
    @JsonProperty(value = "imagesResourceKey")
    private String imagesResourceKey;
    @JsonProperty(value = "serialNumber")
    private String serialNumber;
    @JsonProperty(value = "shelfLife")
    private Date shelfLife;
    @JsonProperty(value = "minStock")
    private Integer minStock;
    @JsonProperty(value = "productStatusEnumProductStatusEnumId1")
    private Integer productStatusEnumProductStatusEnumId1;

}





