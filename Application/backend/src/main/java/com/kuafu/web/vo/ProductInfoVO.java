package com.kuafuweb.web.vo;

import lombok.AllArgsConstructor;
import com.kuafuweb.common.entity.StaticResource;
import java.util.List;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kuafuweb.common.deserializer.MultiDateDeserializer;
import com.kuafuweb.common.entity.BaseEntity;
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
public class ProductInfoVO  extends BaseEntity {

    @JsonProperty("productInfoId")
    private Integer productInfoId;
    @JsonProperty("productName")
    private String productName;
    @JsonProperty("barcode")
    private String barcode;
    @JsonProperty("category")
    private String category;
    @JsonProperty("brand")
    private String brand;
    @JsonProperty("specification")
    private String specification;
    @JsonProperty("unit")
    private String unit;
    @JsonProperty("serialNumber")
    private String serialNumber;
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonProperty("shelfLife")
    private Date shelfLife;
    @JsonProperty("minStock")
    private Integer minStock;
    @JsonProperty("productStatusEnumProductStatusEnumId1")
    private Integer productStatusEnumProductStatusEnumId1;


    private List<StaticResource> images;


}
