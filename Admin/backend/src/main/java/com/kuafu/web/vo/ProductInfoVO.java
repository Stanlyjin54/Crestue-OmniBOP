package com.kuafuweb.web.vo;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.kuafuweb.common.domin.StaticResource;
import java.util.List;

/**
 * <p>商品信息</p>
 *
 * @author kuafuai
 * @description
 * @date 2025/06/18 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfoVO  {

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

    @JsonProperty("images")
    private List<StaticResource> images;

}





